package de.westlotto.accountings.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.accountings.model.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

}
