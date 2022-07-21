package de.westlotto.accountings.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import de.westlotto.accountings.event.AccountingValuesEvent;
import de.westlotto.accountings.event.DrawStateEvent;
import de.westlotto.accountings.event.EventType;
import de.westlotto.accountings.model.AccountingValues;
import de.westlotto.accountings.model.CompanyAccountingValues;
import de.westlotto.accountings.model.CompanyStake;
import de.westlotto.accountings.model.Draw;
import de.westlotto.accountings.model.DrawState;
import de.westlotto.accountings.model.WinnerEvaluationResult;
import de.westlotto.accountings.repository.AccountingValuesRepository;
import de.westlotto.accountings.repository.CompanyStakeRepository;
import de.westlotto.accountings.repository.DrawRepository;
import de.westlotto.accountings.repository.WinnerEvaluationRepository;
import de.westlotto.accountings.service.AccountingService;

@Service
@Transactional
public class AccountingDefaultService implements AccountingService {

        @Autowired
        private AccountingValuesRepository accountingValuesRepository;

        @Autowired
        private CompanyStakeRepository companyStakeRepository;

        @Autowired
        private DrawRepository drawRepository;

        @Autowired
        private WinnerEvaluationRepository winnerEvaluationRepository;

        @Autowired
        private ApplicationEventPublisher applicationEventPublisher;

        @Override
        public AccountingValues findAccountingValues(Long drawId) {
                return accountingValuesRepository.findByDrawId(drawId).orElse(null);
        }

        @Override
        public CompanyAccountingValues findCompanyAccountingValues(Long drawId, Long companyId) {
                AccountingValues accountingValues = accountingValuesRepository.findByDrawId(drawId).orElse(null);
                if (accountingValues == null)
                        return null;

                return accountingValues.getCompanyAccountingValues().stream()
                                .filter(e -> e.getCompany().getId().equals(companyId)).findFirst().orElse(null);
        }

        @Override
        public AccountingValues calculateAccountingValues(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                String.format("No draw with id %d found", drawId)));

                List<WinnerEvaluationResult> winnerEvaluationResults = winnerEvaluationRepository.findByDrawId(drawId);
                if (CollectionUtils.isEmpty(winnerEvaluationResults)) {
                        throw new NoSuchElementException(
                                        String.format("No winner evaluations found for draw id %d", drawId));
                }

                List<CompanyStake> companyStakes = companyStakeRepository.findByDrawId(drawId);
                if (CollectionUtils.isEmpty(companyStakes)) {
                        throw new NoSuchElementException(
                                        String.format("No company stakes found for draw id %d", drawId));
                }

                AccountingValues accountingValues = accountingValuesRepository.findByDrawId(drawId).orElse(null);
                EventType eventType = accountingValues == null ? EventType.CREATE : EventType.UPDATE;
                if (accountingValues == null) {
                        accountingValues = AccountingValues.builder().draw(draw).build();
                }

                accountingValues.calculateAccountings(winnerEvaluationResults, companyStakes);
                accountingValues = accountingValuesRepository.save(accountingValues);
                applicationEventPublisher.publishEvent(
                                AccountingValuesEvent.builder().accountingValues(accountingValues).eventType(eventType)
                                                .build());

                draw.setDrawState(DrawState.ACCOUNTING_CALCULATION);
                draw = drawRepository.save(draw);
                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().drawId(draw.getId()).eventType(EventType.UPDATE)
                                                .drawState(DrawState.ACCOUNTING_CALCULATION)
                                                .build());

                return accountingValues;
        }

        public AccountingValues approve(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                String.format("No draw with id %d found", drawId)));

                AccountingValues accountingValues = accountingValuesRepository.findByDrawId(drawId)
                                .orElseThrow(() -> new NoSuchElementException(
                                                String.format("No accounting values found for draw id %d", drawId)));

                accountingValues.approve();
                accountingValues = accountingValuesRepository.save(accountingValues);
                applicationEventPublisher.publishEvent(
                                AccountingValuesEvent.builder().accountingValues(accountingValues)
                                                .eventType(EventType.UPDATE).build());

                draw.setDrawState(DrawState.ACCOUNTING_CALCULATION_CONFIRMED);
                draw = drawRepository.save(draw);
                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().drawId(draw.getId()).eventType(EventType.UPDATE)
                                                .drawState(DrawState.ACCOUNTING_CALCULATION_CONFIRMED)
                                                .build());

                return accountingValues;
        }

}
