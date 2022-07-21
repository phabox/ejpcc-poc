package de.westlotto.combifiles.event.listener;

import de.westlotto.combifiles.event.CombifileEvent;
import de.westlotto.combifiles.event.CompanyDrawStateEvent;
import de.westlotto.combifiles.event.CompanyEvent;
import de.westlotto.combifiles.event.DrawEvent;
import de.westlotto.combifiles.event.DrawStateEvent;
import de.westlotto.combifiles.event.FallbackEvent;

public interface KafkaInboxEventHandler {

    void handleCompanyEvent(CompanyEvent event);

    void handleDrawEvent(DrawEvent event);

    void handleDrawStateEvent(DrawStateEvent event);

    void handleCompanyDrawStateEvent(CompanyDrawStateEvent event);

    void handleCombifileEvent(CombifileEvent event);

    void handleFallbackEvent(FallbackEvent event);
}
