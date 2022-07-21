package de.westlotto.winningnumbers.event;

import de.westlotto.winningnumbers.event.listener.KafkaInboxEventHandler;
import de.westlotto.winningnumbers.model.DrawState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class DrawStateEvent extends AbstractEvent {
    private Long drawId;
    private DrawState drawState;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleDrawStateEvent(this);
    }
}
