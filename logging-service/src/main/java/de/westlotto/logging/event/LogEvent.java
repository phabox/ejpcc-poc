package de.westlotto.logging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogEvent {

    String serviceName;

    String time;

    Direction direction;

    String action;

    String eventName;

    Object event;

    EventType type;

    public enum Direction {
        SEND,
        RECEIVE
    }
}
