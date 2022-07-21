package de.westlotto.winningnumbers.service;

import de.westlotto.winningnumbers.event.AbstractEvent;
import de.westlotto.winningnumbers.event.LogEvent;

public interface KafkaProducerService {
 
    public void sendEvent(String topic, AbstractEvent event);
   
    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
