package de.westlotto.winnings.event;

import de.westlotto.winnings.event.listener.KafkaInboxEventHandler;
import de.westlotto.winnings.model.WinningNumbers;
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
public class WinningNumbersEvent extends AbstractEvent {

    private WinningNumbers winningNumbers;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleWinningNumbersEvent(this);
    }

}
