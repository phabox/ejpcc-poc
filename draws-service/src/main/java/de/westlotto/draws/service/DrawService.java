package de.westlotto.draws.service;

import java.util.Optional;
import de.westlotto.draws.model.Draw;
import de.westlotto.draws.model.DrawState;

public interface DrawService {

    public Optional<Draw> findById(Long id);

    public Iterable<Draw> findAll();

    public Draw createDraw(Draw draw);

    public Draw updateDraw(Draw draw);

    public Draw changeDrawState(Long drawId, DrawState state);

    public Draw companyReady(Long drawId, Long companyId);
}
