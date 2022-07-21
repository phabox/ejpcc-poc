package de.westlotto.logging.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.logging.model.LogMessage;

@Repository
public interface LogRepository extends CrudRepository<LogMessage, Integer>{
    
}
