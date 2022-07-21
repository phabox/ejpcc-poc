package de.westlotto.winningnumbers.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import de.westlotto.winningnumbers.event.DrawStateEvent;
import de.westlotto.winningnumbers.event.WinningNumbersEvent;
import de.westlotto.winningnumbers.service.KafkaProducerService;

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
    public void onWinningNumbersChange(WinningNumbersEvent event) {
        kafkaProducerService.sendEvent(EJPCC_TOPIC, event);
    }

}
