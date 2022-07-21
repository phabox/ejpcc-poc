package de.westlotto.companies.event;

import de.westlotto.companies.event.listener.KafkaInboxEventHandler;
import de.westlotto.companies.model.Company;
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
