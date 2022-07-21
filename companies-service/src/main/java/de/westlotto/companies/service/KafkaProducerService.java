package de.westlotto.companies.service;

import de.westlotto.companies.event.AbstractEvent;
import de.westlotto.companies.event.LogEvent;

public interface KafkaProducerService {

    public void sendEvent(String topic, AbstractEvent event);

    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
