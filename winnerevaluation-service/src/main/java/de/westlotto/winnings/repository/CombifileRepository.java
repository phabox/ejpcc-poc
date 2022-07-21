package de.westlotto.winnings.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.winnings.model.Combifile;

@Repository
public interface CombifileRepository extends CrudRepository<Combifile, Long> {

    Optional<Combifile> findFirstByCompanyIdAndDrawId(Long companyId, Long drawId);
}
