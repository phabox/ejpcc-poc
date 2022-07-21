package de.westlotto.draws.event.listener;

import de.westlotto.draws.event.CompanyDrawStateEvent;
import de.westlotto.draws.event.CompanyEvent;
import de.westlotto.draws.event.DrawEvent;
import de.westlotto.draws.event.DrawStateEvent;
import de.westlotto.draws.event.FallbackEvent;

public interface KafkaInboxEventHandler {

    void handleCompanyEvent(CompanyEvent event);

    void handleDrawEvent(DrawEvent event);

    void handleDrawStateEvent(DrawStateEvent event);

    void handleCompanyDrawStateEvent(CompanyDrawStateEvent event);

    void handleFallbackEvent(FallbackEvent event);
}
