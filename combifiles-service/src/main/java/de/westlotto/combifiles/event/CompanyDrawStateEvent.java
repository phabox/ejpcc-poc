package de.westlotto.combifiles.event;

import de.westlotto.combifiles.event.listener.KafkaInboxEventHandler;
import de.westlotto.combifiles.model.CompanyState;
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
public class CompanyDrawStateEvent extends AbstractEvent {
    private Long drawId;
    private Long companyId;
    private CompanyState companyState;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleCompanyDrawStateEvent(this);
    }
}
