package de.westlotto.combifiles.service;

import java.util.Optional;

import de.westlotto.combifiles.model.Draw;

public interface DrawService {
    public Draw save(Draw draw);

    public Optional<Draw> findById(Long id);

    //public Optional<Draw> findByDrawId(Long drawId);

    public Iterable<Draw> findAll();
}
