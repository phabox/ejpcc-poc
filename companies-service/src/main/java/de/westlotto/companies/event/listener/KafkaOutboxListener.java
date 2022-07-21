package de.westlotto.companies.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import de.westlotto.companies.event.CompanyEvent;
import de.westlotto.companies.service.KafkaProducerService;

@Component
public class KafkaOutboxListener {

    @Value("${kafka.ejpcc.topic}")
    private String EJPCC_TOPIC;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompanyChange(CompanyEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };

}
