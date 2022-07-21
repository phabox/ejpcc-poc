package de.westlotto.combifiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.westlotto.combifiles.model.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {

}
