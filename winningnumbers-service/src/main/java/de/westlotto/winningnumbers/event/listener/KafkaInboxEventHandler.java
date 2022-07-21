package de.westlotto.winningnumbers.event.listener;

import de.westlotto.winningnumbers.event.DrawEvent;
import de.westlotto.winningnumbers.event.DrawStateEvent;
import de.westlotto.winningnumbers.event.FallbackEvent;
import de.westlotto.winningnumbers.event.WinningNumbersEvent;
public interface KafkaInboxEventHandler {

    void handleDrawEvent(DrawEvent event);

    void handleDrawStateEvent(DrawStateEvent event);

    void handleFallbackEvent(FallbackEvent event);

    void handleWinningNumbersEvent(WinningNumbersEvent event);
}
