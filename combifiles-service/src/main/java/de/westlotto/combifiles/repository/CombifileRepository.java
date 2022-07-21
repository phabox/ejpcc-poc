package de.westlotto.combifiles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.westlotto.combifiles.model.Combifile;

@Repository
public interface CombifileRepository extends CrudRepository<Combifile, Long> {

    Optional<Combifile> findFirstByCompanyIdAndDrawId(Long companyId, Long drawId);

    List<Combifile> findByDrawId(Long drawId);
}
