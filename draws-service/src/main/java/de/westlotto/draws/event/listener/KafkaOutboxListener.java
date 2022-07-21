package de.westlotto.draws.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import de.westlotto.draws.event.CompanyDrawStateEvent;
import de.westlotto.draws.event.CompanyEvent;
import de.westlotto.draws.event.DrawEvent;
import de.westlotto.draws.event.DrawStateEvent;
import de.westlotto.draws.service.KafkaProducerService;

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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDrawChange(DrawEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDrawStateChange(DrawStateEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompanyDrawStateChange(CompanyDrawStateEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };
}
