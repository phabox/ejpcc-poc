package de.westlotto.accountings.event.listener;

import de.westlotto.accountings.event.AccountingValuesEvent;
import de.westlotto.accountings.event.CombifileEvent;
import de.westlotto.accountings.event.CompanyDrawStateEvent;
import de.westlotto.accountings.event.CompanyEvent;
import de.westlotto.accountings.event.DrawEvent;
import de.westlotto.accountings.event.DrawStateEvent;
import de.westlotto.accountings.event.FallbackEvent;
import de.westlotto.accountings.event.WinnerEvaluationEvent;

public interface KafkaInboxEventHandler {

    void handleCompanyEvent(CompanyEvent event);

    void handleDrawEvent(DrawEvent event);

    void handleDrawStateEvent(DrawStateEvent event);

    void handleCompanyDrawStateEvent(CompanyDrawStateEvent event);

    void handleCombifileEvent(CombifileEvent event);

    void handleWinnerEvaluationEvent(WinnerEvaluationEvent event);

    void handleAccountingValuesEvent(AccountingValuesEvent event);

    void handleFallbackEvent(FallbackEvent event);
}
