package de.westlotto.companies.event.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.companies.event.AbstractEvent;
import de.westlotto.companies.event.CompanyEvent;
import de.westlotto.companies.event.FallbackEvent;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class KafkaInboxListener implements KafkaInboxEventHandler {

    private static final String INCOMING_EVENT_MESSAGE = "Received event for topic '{}' with data: {}";
    private static final String NO_ACTION_MESSAGE = "No action required for event {}";

    @KafkaListener(topics = "${kafka.ejpcc.topic}")
    public void incomingEvent(AbstractEvent event, ConsumerRecordMetadata meta) {
        log.info(INCOMING_EVENT_MESSAGE, meta.topic(), event);
        event.accept(this);
    }

    @Override
    public void handleCompanyEvent(CompanyEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

    @Override
    public void handleFallbackEvent(FallbackEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

}
