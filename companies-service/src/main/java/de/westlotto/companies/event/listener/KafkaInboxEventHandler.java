package de.westlotto.companies.event.listener;


import de.westlotto.companies.event.CompanyEvent;
import de.westlotto.companies.event.FallbackEvent;

public interface KafkaInboxEventHandler {

    void handleFallbackEvent(FallbackEvent event);

    void handleCompanyEvent(CompanyEvent companyEvent);
}
