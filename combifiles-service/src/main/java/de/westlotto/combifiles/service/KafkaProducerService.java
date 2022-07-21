package de.westlotto.combifiles.service;

import de.westlotto.combifiles.event.AbstractEvent;
import de.westlotto.combifiles.event.LogEvent;

public interface KafkaProducerService {
 
    public void sendEvent(String topic, AbstractEvent event);
 
    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
