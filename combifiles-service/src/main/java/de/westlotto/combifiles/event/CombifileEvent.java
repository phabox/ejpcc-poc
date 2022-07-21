package de.westlotto.combifiles.event;

import de.westlotto.combifiles.event.listener.KafkaInboxEventHandler;
import de.westlotto.combifiles.model.Combifile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CombifileEvent extends AbstractEvent {

    private Combifile combifile;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleCombifileEvent(this);
    }

}
