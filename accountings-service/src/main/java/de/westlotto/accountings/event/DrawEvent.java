package de.westlotto.accountings.event;

import de.westlotto.accountings.event.listener.KafkaInboxEventHandler;
import de.westlotto.accountings.model.Draw;
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
public class DrawEvent extends AbstractEvent {
    private Draw draw;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleDrawEvent(this);
    }
}
