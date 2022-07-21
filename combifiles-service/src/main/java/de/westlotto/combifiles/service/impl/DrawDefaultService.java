package de.westlotto.combifiles.service.impl;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import de.westlotto.combifiles.model.Draw;
import de.westlotto.combifiles.repository.DrawRepository;
import de.westlotto.combifiles.service.DrawService;

@Service
@Transactional
public class DrawDefaultService implements DrawService {
    Logger log = LoggerFactory.getLogger(DrawDefaultService.class);

    @Autowired
    private DrawRepository drawRepository;

    @Override
    public Draw save(Draw draw) {
        return drawRepository.save(draw);
    }

    @Override
    public Optional<Draw> findById(Long id) {
        return drawRepository.findById(id);
    }

    @Override
    public Iterable<Draw> findAll() {
        return drawRepository.findAll();
    }

}
