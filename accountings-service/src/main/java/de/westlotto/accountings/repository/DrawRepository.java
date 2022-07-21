package de.westlotto.accountings.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.accountings.model.Draw;

@Repository
public interface DrawRepository extends CrudRepository<Draw, Long> {

}
