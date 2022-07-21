package de.westlotto.accountings.event;

import de.westlotto.accountings.event.listener.KafkaInboxEventHandler;
import de.westlotto.accountings.model.AccountingValues;
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
public class AccountingValuesEvent extends AbstractEvent {

    private AccountingValues accountingValues;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleAccountingValuesEvent(this);
    }

}
