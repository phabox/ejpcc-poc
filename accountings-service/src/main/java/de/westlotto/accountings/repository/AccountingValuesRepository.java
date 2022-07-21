package de.westlotto.accountings.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.westlotto.accountings.model.AccountingValues;

public interface AccountingValuesRepository extends CrudRepository<AccountingValues, Long> {

    Optional<AccountingValues> findByDrawId(Long drawId);

}
