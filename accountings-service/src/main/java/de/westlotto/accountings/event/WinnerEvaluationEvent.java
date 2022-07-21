package de.westlotto.accountings.event;

import java.util.List;

import de.westlotto.accountings.event.listener.KafkaInboxEventHandler;
import de.westlotto.accountings.model.WinnerEvaluationResult;
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
public class WinnerEvaluationEvent extends AbstractEvent {

    private List<WinnerEvaluationResult> winnerEvaluationResults;

    @Override
    public void accept(KafkaInboxEventHandler eventHandler) {
        eventHandler.handleWinnerEvaluationEvent(this);
    }
}
