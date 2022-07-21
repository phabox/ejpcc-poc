package de.westlotto.winnings.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.winnings.model.Draw;

@Repository
public interface DrawRepository extends CrudRepository<Draw, Long> {

}
