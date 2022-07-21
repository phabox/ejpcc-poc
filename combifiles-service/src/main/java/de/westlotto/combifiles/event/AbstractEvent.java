package de.westlotto.combifiles.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import de.westlotto.combifiles.event.listener.KafkaInboxEventHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(use = Id.DEDUCTION, defaultImpl = FallbackEvent.class)
// Events, that should be handled
@JsonSubTypes({
        @Type(CompanyDrawStateEvent.class),
        @Type(CompanyEvent.class),
        @Type(DrawEvent.class),
        @Type(DrawStateEvent.class)
})
public abstract class AbstractEvent {
    protected EventType eventType;

    public abstract void accept(KafkaInboxEventHandler eventHandler);
}
