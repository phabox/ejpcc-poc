package de.westlotto.winningnumbers.event.listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.winningnumbers.event.AbstractEvent;
import de.westlotto.winningnumbers.event.DrawEvent;
import de.westlotto.winningnumbers.event.DrawStateEvent;
import de.westlotto.winningnumbers.event.EventType;
import de.westlotto.winningnumbers.event.FallbackEvent;
import de.westlotto.winningnumbers.event.LogEvent;
import de.westlotto.winningnumbers.event.WinningNumbersEvent;
import de.westlotto.winningnumbers.model.Draw;
import de.westlotto.winningnumbers.repository.DrawRepository;
import de.westlotto.winningnumbers.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class KafkaInboxListener implements KafkaInboxEventHandler {

    private static final String INCOMING_EVENT_MESSAGE = "Received event for topic '{}' with data: {}";
    private static final String NO_ACTION_MESSAGE = "No action required for event {}";

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "${kafka.ejpcc.topic}")
    public void incomingEvent(AbstractEvent event, ConsumerRecordMetadata meta) {
        log.info(INCOMING_EVENT_MESSAGE, meta.topic(), event);
        event.accept(this);
    }

    @Override
    public void handleDrawEvent(DrawEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            log.info("Delete draw with id {}", event.getDraw().getId());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete draw", event);
            drawRepository.deleteById(event.getDraw().getId());
        } else {
            log.info("Save draw {}", event.getDraw());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save draw", event);
            drawRepository.save(event.getDraw());
        }
    }

    @Override
    public void handleDrawStateEvent(DrawStateEvent event) {
        Optional<Draw> draw = drawRepository.findById(event.getDrawId());
        draw.ifPresentOrElse(d -> {
            d.setDrawState(event.getEventType() == EventType.DELETE ? null : event.getDrawState());
            log.info("Save draw state {} for draw {}", event.getDrawState(), event.getDrawId());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save draw state", event);
            drawRepository.save(d);
        }, () -> {
            log.error("Error updating draw state: draw with drawId {} not found; event: {}", event.getDrawId(), event);
        });
    }


    @Override
    public void handleWinningNumbersEvent(WinningNumbersEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

    @Override
    public void handleFallbackEvent(FallbackEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

}
