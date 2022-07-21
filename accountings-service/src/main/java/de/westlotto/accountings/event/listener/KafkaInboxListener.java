package de.westlotto.accountings.event.listener;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.accountings.event.AbstractEvent;
import de.westlotto.accountings.event.AccountingValuesEvent;
import de.westlotto.accountings.event.CombifileEvent;
import de.westlotto.accountings.event.CompanyDrawStateEvent;
import de.westlotto.accountings.event.CompanyEvent;
import de.westlotto.accountings.event.DrawEvent;
import de.westlotto.accountings.event.DrawStateEvent;
import de.westlotto.accountings.event.EventType;
import de.westlotto.accountings.event.FallbackEvent;
import de.westlotto.accountings.event.LogEvent;
import de.westlotto.accountings.event.WinnerEvaluationEvent;
import de.westlotto.accountings.model.Company;
import de.westlotto.accountings.model.CompanyStake;
import de.westlotto.accountings.model.Draw;
import de.westlotto.accountings.model.WinnerEvaluationResult;
import de.westlotto.accountings.repository.CompanyRepository;
import de.westlotto.accountings.repository.CompanyStakeRepository;
import de.westlotto.accountings.repository.DrawRepository;
import de.westlotto.accountings.repository.WinnerEvaluationRepository;
import de.westlotto.accountings.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class KafkaInboxListener implements KafkaInboxEventHandler {

    private static final String INCOMING_EVENT_MESSAGE = "Received event for topic '{}' with data: {}";
    private static final String NO_ACTION_MESSAGE = "No action required for event {}";

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private CompanyStakeRepository companyStakeRepository;

    @Autowired
    private WinnerEvaluationRepository winnerEvaluationRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "${kafka.ejpcc.topic}")
    public void incomingEvent(AbstractEvent event, ConsumerRecordMetadata meta) {
        log.info(INCOMING_EVENT_MESSAGE, meta.topic(), event);
        event.accept(this);
    }

    @Override
    public void handleCompanyEvent(CompanyEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete company", event);
            companyRepository.deleteById(event.getCompany().getId());
        } else {
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save company", event);
            companyRepository.save(event.getCompany());
        }
    }

    @Override
    public void handleDrawEvent(DrawEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            log.info("Delete draw with id {}", event.getDraw().getId());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete draw", event);
            drawRepository.deleteById(event.getDraw().getId());
        } else {
            log.info("Save draw {}", event.getDraw());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save draw", event);
            drawRepository.save(event.getDraw());
        }
    }

    @Override
    public void handleDrawStateEvent(DrawStateEvent event) {
        Optional<Draw> draw = drawRepository.findById(event.getDrawId());
        draw.ifPresentOrElse(d -> {
            d.setDrawState(event.getEventType() == EventType.DELETE ? null : event.getDrawState());
            log.info("Save draw state {} for draw {}", event.getDrawState(), event.getDrawId());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save draw state", event);
            drawRepository.save(d);
        }, () -> {
            log.error("Error updating draw state: draw with drawId {} not found; event: {}", event.getDrawId(), event);
        });
    }

    @Override
    public void handleCompanyDrawStateEvent(CompanyDrawStateEvent event) {
        Optional<Draw> draw = drawRepository.findById(event.getDrawId());
        draw.ifPresentOrElse(d -> {
            d.getCompanyDrawStates().stream().filter(s -> event.getCompanyId().equals(s.getCompany().getId()))
                    .findFirst()
                    .ifPresentOrElse((ds -> {
                        ds.setCompanyState(event.getCompanyState());
                        this.drawRepository.save(d);
                        kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE,
                                "save company daw state", event);
                    }), () -> log.error("Error updating company draw state: no company with id {} found; event: {}",
                            event.getCompanyId(), event));
        }, () -> log.error(
                "Error updating company draw state: draw with drawId {} not found; event: {}", event.getDrawId(),
                event));
    }

    @Override
    public void handleCombifileEvent(CombifileEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            // Delete company stake
            log.info("Delete company stake for draw id {} and company id {}", event.getCombifile().getDraw().getId(),
                    event.getCombifile().getCompanyId());
            companyStakeRepository.deleteByDrawIdAndCompanyId(event.getCombifile().getDraw().getId(),
                    event.getCombifile().getCompanyId());
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete stake for company", event);
        } else {
            // Save company stake
            log.info("Save stake {} for company id {} and draw id {}", event.getCombifile().getAmount(),
                    event.getCombifile().getCompanyId(), event.getCombifile().getDraw().getId());
            CompanyStake companyStake = companyStakeRepository.findFirstByDrawIdAndCompanyId(
                    event.getCombifile().getDraw().getId(), event.getCombifile().getCompanyId()).orElse(null);
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "update company stake", event);
            if (companyStake != null) {
                companyStake.setStake(event.getCombifile().getAmount());
            } else {
                companyStake = CompanyStake.builder().draw(event.getCombifile().getDraw()).company(new Company(
                        event.getCombifile().getCompanyId())).stake(event.getCombifile().getAmount()).build();
            }
            companyStakeRepository.save(companyStake);
        }
    }

    @Override
    public void handleWinnerEvaluationEvent(WinnerEvaluationEvent event) {
        if (event.getEventType() == EventType.DELETE) {
            log.info("Delete winner evaluations with ids {}",
                    event.getWinnerEvaluationResults().stream().map(WinnerEvaluationResult::getId)
                            .collect(Collectors.toList()));
            drawRepository.deleteAllById(event.getWinnerEvaluationResults().stream().map(WinnerEvaluationResult::getId)
                    .collect(Collectors.toList()));
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "delete winner evaluations", event);
        } else {
            log.info("Save winner evaluations {}", event.getWinnerEvaluationResults());
            event.getWinnerEvaluationResults().forEach(winnerEvaluationRepository::save);
            kafkaProducerService.sendLogEvent(LogEvent.Direction.RECEIVE, "save winner evaluations", event);
        }
    }

    @Override
    public void handleAccountingValuesEvent(AccountingValuesEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

    @Override
    public void handleFallbackEvent(FallbackEvent event) {
        log.info(NO_ACTION_MESSAGE, event);
    }

}
