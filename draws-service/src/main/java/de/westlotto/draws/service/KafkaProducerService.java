package de.westlotto.draws.service;

import de.westlotto.draws.event.AbstractEvent;
import de.westlotto.draws.event.LogEvent;

public interface KafkaProducerService {

    public void sendEvent(String topic, AbstractEvent event);

    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
