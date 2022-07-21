package de.westlotto.winningnumbers.event;

import de.westlotto.winningnumbers.event.listener.KafkaInboxEventHandler;

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
    private String companyState;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        ;
    }
}
