package de.westlotto.winnings.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.winnings.model.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

}
