package de.westlotto.winnings.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import de.westlotto.winnings.event.AbstractEvent;
import de.westlotto.winnings.event.LogEvent;
import de.westlotto.winnings.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaProducerDefaultService implements KafkaProducerService {

    @Value("${kafka.logs.topic}")
    private String LOG_TOPIC;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendEvent(String topic, AbstractEvent event) {
        try {
            log.info("Trying to send event {} to Kafka topic {}", event, topic);
            kafkaTemplate.send(topic, event).get(5, TimeUnit.SECONDS);
            this.sendLogEvent(LogEvent.Direction.SEND, null, event);
            log.info("Successfully sent event {} to Kafka topic {}", event, topic);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error sending event {} to Kafka topic {}: {}", event, topic, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event) {
        try {
            kafkaTemplate.send(LOG_TOPIC,
                    new LogEvent("WinnerEvaluation",
                            DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(LocalDateTime.now()), direction, action,
                            event.getClass().getSimpleName(), event, event.getEventType()))
                    .get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error sending log event to Kafka topic {}: {}", LOG_TOPIC, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}