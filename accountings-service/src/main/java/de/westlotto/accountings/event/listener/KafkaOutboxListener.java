package de.westlotto.accountings.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import de.westlotto.accountings.event.AccountingValuesEvent;
import de.westlotto.accountings.event.CompanyDrawStateEvent;
import de.westlotto.accountings.event.DrawStateEvent;
import de.westlotto.accountings.event.WinnerEvaluationEvent;
import de.westlotto.accountings.service.KafkaProducerService;

@Component
public class KafkaOutboxListener {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${kafka.ejpcc.topic}")
    private String EJPCC_TOPIC;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDrawStateChange(DrawStateEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompanyDrawStateChange(CompanyDrawStateEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    };

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onWinnerEvaluationChange(WinnerEvaluationEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAccountingValuesChange(AccountingValuesEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    }

}
