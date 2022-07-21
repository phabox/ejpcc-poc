package de.westlotto.draws.event.listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.draws.event.AbstractEvent;
import de.westlotto.draws.event.CompanyDrawStateEvent;
import de.westlotto.draws.event.CompanyEvent;
import de.westlotto.draws.event.DrawEvent;
import de.westlotto.draws.event.DrawStateEvent;
import de.westlotto.draws.event.EventType;
import de.westlotto.draws.event.FallbackEvent;
import de.westlotto.draws.event.LogEvent;
import de.westlotto.draws.model.Draw;
import de.westlotto.draws.repository.CompanyRepository;
import de.westlotto.draws.repository.DrawRepository;
import de.westlotto.draws.service.KafkaProducerService;
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
    private CompanyRepository companyRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "${kafka.ejpcc.topic}")
    public void incomingEvent(AbstractEvent event, ConsumerRecordMetadata meta) {
        log.info(INCOMING_EVENT_MESSAGE, meta.topic(), event);
        event.accept(this);
    }

    @Override
    public void handleDrawStateEvent(DrawStateEvent event) {
        Optional<Draw> draw = drawRepository.findById(event.getDrawId());
        draw.ifPresentOrElse(d -> {
            d.setDrawState(event.getEventType() == EventType.DELETE ? null : event.getDrawState());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save draw state", event);
            drawRepository.save(d);
        }, () -> {
            log.error("Error updating draw state: draw with drawId {} not found; event: {}", event.getDrawId(), event);
        });
    }

    @Override
    public void handleCompanyDrawStateEvent(CompanyDrawStateEvent event) {
        Optional<Draw> draw = drawRepository.findById(event.getDrawId());
        draw.ifPresentOrElse(d -> {
            d.getCompanyDrawStates().stream().filter(s -> event.getCompanyId().equals(s.getCompany().getId()))
                    .findFirst()
                    .ifPresentOrElse((ds -> {
                        ds.setCompanyState(event.getCompanyState());
                        kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save company draw state", event);
                        this.drawRepository.save(d);
                    }), () -> log.error("Error updating company draw state: no company with id {} found; event: {}",
                            event.getCompanyId(), event));
        }, () -> log.error(
                "Error updating company draw state: draw with drawId {} not found; event: {}", event.getDrawId(),
                event));
    }

    @Override
    public void handleCompanyEvent(CompanyEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete company", event);
            companyRepository.deleteById(event.getCompany().getId());
        } else {
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save company", event);
            companyRepository.save(event.getCompany());
        }
    }

    @Override
    public void handleDrawEvent(DrawEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

    @Override
    public void handleFallbackEvent(FallbackEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

}
