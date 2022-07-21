package de.westlotto.winnings.service;

import de.westlotto.winnings.event.AbstractEvent;
import de.westlotto.winnings.event.LogEvent;

public interface KafkaProducerService {
 
    public void sendEvent(String topic, AbstractEvent event);
    
    void sendLogEvent(LogEvent.Direction direction, String action, AbstractEvent event);
}
