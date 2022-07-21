package de.westlotto.draws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.draws.model.Draw;

@Repository
public interface DrawRepository extends CrudRepository<Draw, Long> {
    
}
