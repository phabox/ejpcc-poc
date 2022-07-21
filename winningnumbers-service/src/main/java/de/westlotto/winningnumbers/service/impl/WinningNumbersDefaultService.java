package de.westlotto.winningnumbers.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.winningnumbers.event.DrawStateEvent;
import de.westlotto.winningnumbers.event.EventType;
import de.westlotto.winningnumbers.event.WinningNumbersEvent;
import de.westlotto.winningnumbers.model.Draw;
import de.westlotto.winningnumbers.model.DrawState;
import de.westlotto.winningnumbers.model.WinningNumber;
import de.westlotto.winningnumbers.model.WinningNumbers;
import de.westlotto.winningnumbers.repository.DrawRepository;
import de.westlotto.winningnumbers.repository.WinningNumbersRepository;
import de.westlotto.winningnumbers.service.WinningNumbersService;

@Service
@Transactional
public class WinningNumbersDefaultService implements WinningNumbersService {

        @Autowired
        private WinningNumbersRepository winningNumbersRepository;

        @Autowired
        private DrawRepository drawRepository;

        @Autowired
        private ApplicationEventPublisher applicationEventPublisher;

        @Override
        public WinningNumbers findWinningNumbers(Long drawId) {
                return winningNumbersRepository.findFirstByDrawId(drawId).orElse(null);
        }

        @Override
        public WinningNumbers saveWinningNumbers(Long drawId, List<WinningNumber> winningNumbers) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));

                WinningNumbers wnr = winningNumbersRepository.findFirstByDrawId(drawId).orElse(null);
                EventType wnrEventType = wnr == null ? EventType.CREATE : EventType.UPDATE;
                if (wnr == null) {
                        wnr = WinningNumbers.builder().draw(draw).build();
                }
                wnr.setWinningNumbers(winningNumbers);
                draw.setDrawState(DrawState.WINNING_NUMBERS_RECEIVED);

                wnr = winningNumbersRepository.save(wnr);
                draw = drawRepository.save(draw);

                applicationEventPublisher.publishEvent(WinningNumbersEvent.builder()
                                .eventType(wnrEventType).winningNumbers(wnr).build());
                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().eventType(EventType.UPDATE).drawId(draw.getId())
                                                .drawState(draw.getDrawState()).build());

                return wnr;
        }

        @Override
        public WinningNumbers approveWinningNumbers(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));
                WinningNumbers wnr = winningNumbersRepository.findFirstByDrawId(drawId).orElseThrow(
                                () -> new RuntimeException(
                                                String.format("No winning numbers for draw id %d found", drawId)));

                wnr.approve(draw, true);
                draw.setDrawState(DrawState.WINNING_NUMBERS_CONFIRMED);

                wnr = winningNumbersRepository.save(wnr);
                draw = drawRepository.save(draw);

                applicationEventPublisher.publishEvent(WinningNumbersEvent.builder()
                                .eventType(EventType.UPDATE).winningNumbers(wnr).build());
                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().eventType(EventType.UPDATE).drawId(draw.getId())
                                                .drawState(draw.getDrawState()).build());

                return wnr;
        }

        @Override
        public void readyToReceive(Long drawId) {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No draw with id %d found", drawId)));

                draw.setDrawState(DrawState.ACCEPT_WINNING_NUMBERS);
                draw = drawRepository.save(draw);

                applicationEventPublisher
                                .publishEvent(DrawStateEvent.builder().eventType(EventType.UPDATE).drawId(draw.getId())
                                                .drawState(draw.getDrawState()).build());
        }

}
