package de.westlotto.combifiles.event;

import de.westlotto.combifiles.event.listener.KafkaInboxEventHandler;
import de.westlotto.combifiles.model.Company;
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
