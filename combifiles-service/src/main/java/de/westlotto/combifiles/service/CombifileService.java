package de.westlotto.combifiles.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import de.westlotto.combifiles.model.Combifile;

public interface CombifileService {
    Combifile save(Combifile combifile);

    public Optional<Combifile> findCombifile(Long drawId, Long companyId);

    public List<Combifile> findCombifiles(Long drawId);

    public Combifile processFileUpload(Long drawId, Long companyId, MultipartFile combifile) throws IOException;

    public Combifile confirmMetadata(Long drawId, Long companyId, boolean confirmed);

    public Combifile approveCombifile(Long drawId, Long companyId, boolean approved);
}
