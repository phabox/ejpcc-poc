package de.westlotto.draws.event;

import de.westlotto.draws.event.listener.KafkaInboxEventHandler;
import de.westlotto.draws.model.Company;
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
public class CompanyEvent extends AbstractEvent {
    Company company;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleCompanyEvent(this);
    }
}
