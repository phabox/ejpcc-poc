package de.westlotto.companies.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import de.westlotto.companies.model.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long>{

}
