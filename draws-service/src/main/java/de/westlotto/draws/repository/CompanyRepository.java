package de.westlotto.draws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import de.westlotto.draws.model.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long>{

}
