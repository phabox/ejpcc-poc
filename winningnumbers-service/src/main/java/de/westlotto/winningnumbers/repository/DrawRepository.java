package de.westlotto.winningnumbers.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.winningnumbers.model.Draw;

@Repository
public interface DrawRepository extends CrudRepository<Draw, Long> {

}
