package de.westlotto.accountings.event;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import de.westlotto.accountings.event.listener.KafkaInboxEventHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FallbackEvent extends AbstractEvent {
    private Map<String, Object> values = new HashMap<>();

    @JsonAnySetter
    public void addValues(String k, Object v) {
        values.put(k, v);
    }

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleFallbackEvent(this);
    }
    
}
