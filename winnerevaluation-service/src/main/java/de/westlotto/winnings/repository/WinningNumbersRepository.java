package de.westlotto.winnings.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.winnings.model.WinningNumbers;

@Repository
public interface WinningNumbersRepository extends CrudRepository<WinningNumbers, Long> {

    Optional<WinningNumbers> findFirstByDrawId(Long drawId);

}
