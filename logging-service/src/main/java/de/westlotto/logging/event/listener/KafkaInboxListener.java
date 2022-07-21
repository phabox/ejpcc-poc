package de.westlotto.logging.event.listener;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.logging.event.LogEvent;
import de.westlotto.logging.model.LogMessage;
import de.westlotto.logging.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class KafkaInboxListener {

    private static final String INCOMING_EVENT_MESSAGE = "Received event for topic '{}' with data: {}";

    @Autowired
    private LogRepository logRepository;

    @KafkaListener(topics = "${kafka.logs.topic}")
    public void incomingEvent(LogEvent event, ConsumerRecordMetadata meta) {
        log.info(INCOMING_EVENT_MESSAGE, meta.topic(), event);
        logRepository.save(LogMessage.builder().serviceName(event.getServiceName()).time(event.getTime())
                .direction(event.getDirection().name()).action(event.getAction()).eventName(event.getEventName())
                .event(new Gson().toJson(event))
                .type(event.getType().name()).build());
    }
}
