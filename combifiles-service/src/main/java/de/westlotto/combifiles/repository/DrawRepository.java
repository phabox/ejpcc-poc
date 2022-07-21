package de.westlotto.combifiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.westlotto.combifiles.model.Draw;

public interface DrawRepository extends CrudRepository<Draw, Long> {
    
}
