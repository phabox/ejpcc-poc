package de.westlotto.winnings.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import de.westlotto.winnings.event.CompanyDrawStateEvent;
import de.westlotto.winnings.event.DrawStateEvent;
import de.westlotto.winnings.event.EventType;
import de.westlotto.winnings.event.WinnerEvaluationEvent;
import de.westlotto.winnings.model.Combifile;
import de.westlotto.winnings.model.Company;
import de.westlotto.winnings.model.CompanyDrawState;
import de.westlotto.winnings.model.CompanyState;
import de.westlotto.winnings.model.Draw;
import de.westlotto.winnings.model.DrawState;
import de.westlotto.winnings.model.WinnerEvaluationResult;
import de.westlotto.winnings.model.WinningNumbers;
import de.westlotto.winnings.repository.CombifileRepository;
import de.westlotto.winnings.repository.DrawRepository;
import de.westlotto.winnings.repository.WinnerEvaluationRepository;
import de.westlotto.winnings.repository.WinningNumbersRepository;
import de.westlotto.winnings.service.WinnerEvaluationService;

@Service
@Transactional
public class WinnerEvaluationDefaultService implements WinnerEvaluationService {

        @Autowired
        private DrawRepository drawRepository;

        @Autowired
        private WinnerEvaluationRepository winnerEvaluationRepository;

        @Autowired
        private WinningNumbersRepository winningNumbersRepository;

        @Autowired
        private CombifileRepository combifileRepository;

        @Autowired
        private ApplicationEventPublisher applicationEventPublisher;

        @Override
        public List<WinnerEvaluationResult> findAll(Long drawId) {
                return winnerEvaluationRepository.findByDrawId(drawId);
        }

        @Override
        public WinnerEvaluationResult findWinnerEvaluation(Long drawId, Long companyId) {
                return winnerEvaluationRepository
                                .findFirstByDrawIdAndCompanyId(drawId, companyId).orElse(null);
        }

        @Override
        public List<WinnerEvaluationResult> runWinnerEvaluation(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));

                Set<Company> companies = draw.getCompanyDrawStates().stream().map(CompanyDrawState::getCompany)
                                .collect(Collectors.toSet());

                WinningNumbers winningNumbers = winningNumbersRepository.findFirstByDrawId(drawId).orElseThrow(
                                () -> new NoSuchElementException(
                                                String.format("No winning numbers found for draw id %d", drawId)));

                List<WinnerEvaluationResult> result = new ArrayList<>();

                for (Company company : companies) {
                        Combifile combifile = combifileRepository
                                        .findFirstByCompanyIdAndDrawId(company.getId(), draw.getId())
                                        .orElseThrow(() -> new NoSuchElementException(String.format(
                                                        "No combifile found for draw id %d and company id %d",
                                                        drawId, company.getId())));

                        WinnerEvaluationResult winnerEvaluation = winnerEvaluationRepository
                                        .findFirstByDrawIdAndCompanyId(drawId, company.getId()).orElse(null);

                        if (winnerEvaluation == null) {
                                winnerEvaluation = WinnerEvaluationResult.builder().draw(draw).company(company).build();
                        }
                        winnerEvaluation.evaluateWinners(combifile, winningNumbers);

                        result.add(winnerEvaluationRepository.save(winnerEvaluation));
                }

                // Aggregation
                WinnerEvaluationResult aggregation = winnerEvaluationRepository
                                .findFirstByDrawIdAndCompanyId(drawId, null)
                                .orElse(null);
                EventType eventType = aggregation == null ? EventType.CREATE : EventType.UPDATE;
                if (aggregation == null) {
                        aggregation = WinnerEvaluationResult.builder().draw(draw).company(null).build();
                }
                aggregation.aggregateWinners(result);
                aggregation = winnerEvaluationRepository.save(aggregation);
                result.add(aggregation);

                applicationEventPublisher.publishEvent(
                                WinnerEvaluationEvent.builder().eventType(eventType).winnerEvaluationResults(result)
                                                .build());

                draw.setDrawState(DrawState.WINNER_EVALUATION);
                draw = drawRepository.save(draw);
                applicationEventPublisher.publishEvent(DrawStateEvent.builder().eventType(EventType.UPDATE)
                                .drawId(draw.getId()).drawState(draw.getDrawState()).build());

                return result;
        }

        @Override
        public WinnerEvaluationResult confirmWinnerEvaluation(Long drawId, Long companyId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));

                WinnerEvaluationResult winnerEvaluation = winnerEvaluationRepository
                                .findFirstByDrawIdAndCompanyId(drawId, companyId)
                                .orElseThrow(() -> new NoSuchElementException(String
                                                .format("No winner evaluation found for draw id %d and company id %d",
                                                                drawId, companyId)));

                winnerEvaluation.approve();
                winnerEvaluation = winnerEvaluationRepository.save(winnerEvaluation);

                draw.getCompanyDrawStates().stream().filter(e -> e.getCompany().getId().equals(companyId)).findFirst()
                                .ifPresentOrElse(companyDrawState -> {
                                        companyDrawState.setCompanyState(CompanyState.WINNINGS_CONFIRMED);
                                        drawRepository.save(draw);
                                        applicationEventPublisher.publishEvent(
                                                        CompanyDrawStateEvent.builder().eventType(EventType.UPDATE)
                                                                        .companyState(CompanyState.WINNINGS_CONFIRMED)
                                                                        .drawId(drawId).companyId(companyId).build());
                                }, () -> {
                                        throw new NoSuchElementException(
                                                        String.format("No company with id %d found in draw with id %d",
                                                                        companyId, draw.getId()));
                                });

                return winnerEvaluation;
        }

        @Override
        public List<WinnerEvaluationResult> approveWinnerEvaluation(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));

                List<WinnerEvaluationResult> winnerEvaluation = winnerEvaluationRepository
                                .findByDrawId(drawId);
                if (CollectionUtils.isEmpty(winnerEvaluation)) {
                        throw new NoSuchElementException(String
                                        .format("No winner evaluation found for draw id %d"));
                }
                WinnerEvaluationResult aggregate = winnerEvaluation.stream().filter(e -> e.getCompany() == null)
                                .findFirst().orElseThrow(() -> new NoSuchElementException(String
                                                .format("No aggregate winner evaluation found for draw id %d",
                                                                drawId)));

                aggregate.approve();
                aggregate = winnerEvaluationRepository.save(aggregate);

                draw.setDrawState(DrawState.WINNER_EVALUATION_CONFIRMED);
                draw = drawRepository.save(draw);

                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().drawId(draw.getId())
                                                .drawState(draw.getDrawState()).eventType(EventType.UPDATE).build());

                return winnerEvaluation;
        }

}
