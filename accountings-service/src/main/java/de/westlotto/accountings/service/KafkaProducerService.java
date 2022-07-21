package de.westlotto.accountings.service;

import de.westlotto.accountings.event.AbstractEvent;
import de.westlotto.accountings.event.LogEvent;

public interface KafkaProducerService {
 
    public void sendEvent(String topic, AbstractEvent event);
    
    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
