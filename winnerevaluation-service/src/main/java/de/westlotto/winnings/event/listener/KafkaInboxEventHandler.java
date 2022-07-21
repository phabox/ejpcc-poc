package de.westlotto.winnings.event.listener;

import de.westlotto.winnings.event.CombifileEvent;
import de.westlotto.winnings.event.CompanyDrawStateEvent;
import de.westlotto.winnings.event.CompanyEvent;
import de.westlotto.winnings.event.DrawEvent;
import de.westlotto.winnings.event.DrawStateEvent;
import de.westlotto.winnings.event.FallbackEvent;
import de.westlotto.winnings.event.WinnerEvaluationEvent;
import de.westlotto.winnings.event.WinningNumbersEvent;

public interface KafkaInboxEventHandler {

    void handleCompanyEvent(CompanyEvent event);

    void handleDrawEvent(DrawEvent event);

    void handleDrawStateEvent(DrawStateEvent event);

    void handleCompanyDrawStateEvent(CompanyDrawStateEvent event);

    void handleCombifileEvent(CombifileEvent event);

    void handleWinningNumbersEvent(WinningNumbersEvent event);

    void handleWinnerEvaluationEvent(WinnerEvaluationEvent event);

    void handleFallbackEvent(FallbackEvent event);

}
