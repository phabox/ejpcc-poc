package de.westlotto.accountings.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.accountings.model.CompanyStake;

@Repository
public interface CompanyStakeRepository extends CrudRepository<CompanyStake, Long> {
    Optional<CompanyStake> findFirstByDrawIdAndCompanyId(Long drawId, Long companyId);

    long deleteByDrawIdAndCompanyId(Long drawId, Long companyId);

    List<CompanyStake> findByDrawId(Long drawId);
}
