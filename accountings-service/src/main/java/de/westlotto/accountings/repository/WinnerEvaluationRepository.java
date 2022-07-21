package de.westlotto.accountings.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.accountings.model.WinnerEvaluationResult;

@Repository
public interface WinnerEvaluationRepository extends CrudRepository<WinnerEvaluationResult, Long> {

    List<WinnerEvaluationResult> findByDrawId(Long drawId);

    Optional<WinnerEvaluationResult> findFirstByDrawIdAndCompanyId(Long drawId, Long companyId);

}
