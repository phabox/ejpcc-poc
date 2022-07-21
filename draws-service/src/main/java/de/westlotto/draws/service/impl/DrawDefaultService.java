package de.westlotto.draws.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.draws.event.CompanyDrawStateEvent;
import de.westlotto.draws.event.DrawEvent;
import de.westlotto.draws.event.DrawStateEvent;
import de.westlotto.draws.event.EventType;
import de.westlotto.draws.model.CompanyDrawState;
import de.westlotto.draws.model.CompanyState;
import de.westlotto.draws.model.Draw;
import de.westlotto.draws.model.DrawState;
import de.westlotto.draws.repository.DrawRepository;
import de.westlotto.draws.service.DrawService;

@Service
@Transactional
public class DrawDefaultService implements DrawService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private DrawRepository drawRepository;

    @Override
    public Optional<Draw> findById(Long id) {
        return drawRepository.findById(id);
    }

    @Override
    public Iterable<Draw> findAll() {
        return drawRepository.findAll();
    }

    private Draw save(Draw draw) {
        return drawRepository.save(draw);
    }

    @Override
    public Draw createDraw(Draw draw) {
        if (draw.getId() != null) {
            throw new IllegalArgumentException(String.format("Draw already has an id: %d", draw.getId()));
        }

        Draw result = this.save(draw);
        this.applicationEventPublisher.publishEvent(
                DrawEvent.builder().draw(result).eventType(EventType.CREATE).build());
        return result;
    }

    @Override
    public Draw updateDraw(Draw draw) {
        if (!drawRepository.existsById(draw.getId())) {
            throw new IllegalArgumentException(String.format("Draw with id %d does not exist", draw.getId()));
        }
        Draw result = this.save(draw);
        this.applicationEventPublisher.publishEvent(DrawEvent.builder().draw(draw).eventType(EventType.UPDATE).build());

        return result;
    }

    @Override
    public Draw changeDrawState(Long drawId, DrawState state) {
        if (state == null) {
            throw new IllegalArgumentException(String.format("Unknown state provided"));
        }

        Draw draw = drawRepository.findById(drawId).orElseThrow(
                () -> new IllegalArgumentException(String.format("Draw with drawId %d does not exist", drawId)));

        draw.setDrawState(state);
        Draw result = this.save(draw);
        this.applicationEventPublisher.publishEvent(
                DrawStateEvent.builder().drawId(drawId).drawState(state).eventType(EventType.UPDATE).build());
        return result;
    }

    @Override
    public Draw companyReady(Long drawId, Long companyId) {
        Draw draw = drawRepository.findById(drawId).orElseThrow(
                () -> new IllegalArgumentException(String.format("Draw with drawId %d does not exist", drawId)));
        if (draw.getDrawState() != DrawState.INIT) {
            throw new IllegalStateException(String.format(
                    "DrawState has to be %s, but it is %s", DrawState.INIT.name(), draw.getDrawState().name()));
        }

        CompanyDrawState companyDrawState = draw.getCompanyDrawStates().stream()
                .filter(ds -> companyId.equals(ds.getCompany().getId())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Cannot find company with id %d in draw with id %d", companyId, drawId)));
        if (companyDrawState.getCompanyState() != CompanyState.NOT_READY) {
            throw new IllegalStateException(String.format(
                    "CompanyState has to be %s, but it is %s", CompanyState.NOT_READY.name(),
                    companyDrawState.getCompanyState().name()));
        }
        companyDrawState.setCompanyState(CompanyState.READY);
        Draw result = this.save(draw);

        this.applicationEventPublisher.publishEvent(CompanyDrawStateEvent.builder().drawId(drawId).companyId(companyId)
                .companyState(CompanyState.READY).eventType(EventType.UPDATE).build());
        return result;
    }

}
