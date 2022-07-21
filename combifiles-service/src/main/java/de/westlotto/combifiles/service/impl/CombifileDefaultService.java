package de.westlotto.combifiles.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import de.westlotto.combifiles.event.CombifileEvent;
import de.westlotto.combifiles.event.CompanyDrawStateEvent;
import de.westlotto.combifiles.event.EventType;
import de.westlotto.combifiles.model.Combifile;
import de.westlotto.combifiles.model.CompanyDrawState;
import de.westlotto.combifiles.model.CompanyState;
import de.westlotto.combifiles.model.Draw;
import de.westlotto.combifiles.repository.CombifileRepository;
import de.westlotto.combifiles.repository.DrawRepository;
import de.westlotto.combifiles.service.CombifileService;
import de.westlotto.combifiles.service.helper.CombifileHelper;

@Service
@Transactional
public class CombifileDefaultService implements CombifileService {

        @Autowired
        private CombifileRepository combifileRepository;

        @Autowired
        private DrawRepository drawRepository;

        @Autowired
        private ApplicationEventPublisher applicationEventPublisher;

        @Autowired
        CombifileHelper combifileHelper;

        @Override
        public Combifile save(Combifile combifile) {
                return combifileRepository.save(combifile);
        }

        @Override
        public Optional<Combifile> findCombifile(Long drawId, Long companyId) {
                return combifileRepository.findFirstByCompanyIdAndDrawId(companyId, drawId);
        }

        @Override
        public List<Combifile> findCombifiles(Long drawId) {
                return combifileRepository.findByDrawId(drawId);
        }

        public Combifile processFileUpload(Long drawId, Long companyId, MultipartFile combifile) throws IOException {
                Draw draw = drawRepository.findById(drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("Unknown draw with id %d", drawId)));

                CompanyDrawState companyDrawState = draw.getCompanyDrawStates().stream()
                                .filter(e -> Objects.equals(e.getCompany().getId(), companyId)).findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No company with id %d found for draw with id %d",
                                                                companyId, drawId)));

                if (!List.of(CompanyState.READY, CompanyState.COMBIFILE_UPLOADED, CompanyState.METADATA_CONFIRMED)
                                .contains(companyDrawState.getCompanyState())) {
                        throw new RuntimeException(String.format("Cannot upload Combifiles while company state is %s",
                                        companyDrawState.getCompanyState().name()));
                }

                Combifile cf = this.combifileRepository.findFirstByCompanyIdAndDrawId(companyId, drawId).orElse(null);
                EventType eventType = cf == null ? EventType.CREATE : EventType.UPDATE;
                if (cf != null) {
                        cf.setFilename(combifile.getOriginalFilename());
                        cf.setType(combifile.getContentType());
                        cf.setAmount(combifileHelper.calculateStake(combifile));
                        cf.setChecksum(DigestUtils.sha256Hex(combifile.getBytes()));
                        cf.setConfirmedByCompany(false);
                        cf.setApprovedByCC(false);
                        cf.setData(combifile.getBytes());
                } else {
                        cf = new Combifile(null, draw, companyId, combifile.getOriginalFilename(),
                                        combifile.getContentType(),
                                        combifileHelper.calculateStake(combifile),
                                        DigestUtils.sha256Hex(combifile.getBytes()), false, false,
                                        combifile.getBytes());
                }

                cf = combifileRepository.save(cf);
                this.applicationEventPublisher
                                .publishEvent(CombifileEvent.builder().combifile(cf).eventType(eventType).build());

                companyDrawState.setCompanyState(CompanyState.COMBIFILE_UPLOADED);
                drawRepository.save(draw);
                this.applicationEventPublisher.publishEvent(
                                CompanyDrawStateEvent.builder().drawId(drawId).companyId(companyId)
                                                .companyState(CompanyState.COMBIFILE_UPLOADED)
                                                .eventType(EventType.UPDATE).build());

                return cf;
        }

        @Override
        public Combifile confirmMetadata(Long drawId, Long companyId, boolean confirmed) {
                Draw draw = drawRepository.findById(drawId).orElseThrow(
                                () -> new RuntimeException(String.format("Unknown draw with id %d", drawId)));

                CompanyDrawState companyDrawState = draw.getCompanyDrawStates().stream()
                                .filter(e -> Objects.equals(e.getCompany().getId(), companyId)).findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No company with id %d found for draw with id %d",
                                                                companyId, drawId)));

                if (!List.of(CompanyState.READY, CompanyState.COMBIFILE_UPLOADED, CompanyState.METADATA_CONFIRMED)
                                .contains(companyDrawState.getCompanyState())) {
                        throw new RuntimeException(String.format("Cannot confirm Combifile while company state is %s",
                                        companyDrawState.getCompanyState().name()));
                }

                Combifile combifile = combifileRepository.findFirstByCompanyIdAndDrawId(companyId, drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No combifile found for drawId %d and companyId %d",
                                                                drawId, companyId)));

                combifile.setConfirmedByCompany(confirmed);
                combifile.setApprovedByCC(false);
                Combifile result = combifileRepository.save(combifile);
                this.applicationEventPublisher.publishEvent(
                                CombifileEvent.builder().combifile(result).eventType(EventType.UPDATE).build());

                companyDrawState.setCompanyState(CompanyState.METADATA_CONFIRMED);
                drawRepository.save(draw);
                this.applicationEventPublisher.publishEvent(
                                CompanyDrawStateEvent.builder().drawId(drawId).companyId(companyId)
                                                .companyState(CompanyState.METADATA_CONFIRMED)
                                                .eventType(EventType.UPDATE).build());

                return result;
        }

        @Override
        public Combifile approveCombifile(Long drawId, Long companyId, boolean approved) {
                Draw draw = drawRepository.findById(drawId).orElseThrow(
                                () -> new RuntimeException(String.format("Unknown draw with id %d", drawId)));

                CompanyDrawState companyDrawState = draw.getCompanyDrawStates().stream()
                                .filter(e -> Objects.equals(e.getCompany().getId(), companyId)).findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No company with id %d found for draw with id %d",
                                                                companyId, drawId)));

                if (!List.of(CompanyState.READY, CompanyState.COMBIFILE_UPLOADED, CompanyState.METADATA_CONFIRMED)
                                .contains(companyDrawState.getCompanyState())) {
                        throw new RuntimeException(String.format("Cannot confirm Combifile while company state is %s",
                                        companyDrawState.getCompanyState().name()));
                }

                Combifile combifile = combifileRepository.findFirstByCompanyIdAndDrawId(companyId, drawId)
                                .orElseThrow(() -> new RuntimeException(
                                                String.format("No combifile found for drawId %d and companyId %d",
                                                                drawId, companyId)));

                if (!combifile.isConfirmedByCompany() && approved) {
                        throw new RuntimeException(
                                        "Combifile metadata has to be confirmed by Company before it can be approved");
                }

                combifile.setApprovedByCC(approved);
                Combifile result = combifileRepository.save(combifile);
                this.applicationEventPublisher.publishEvent(
                                CombifileEvent.builder().combifile(result).eventType(EventType.UPDATE).build());

                companyDrawState.setCompanyState(CompanyState.COMBIFILE_APPROVED);
                drawRepository.save(draw);
                this.applicationEventPublisher.publishEvent(
                                CompanyDrawStateEvent.builder().drawId(drawId).companyId(companyId)
                                                .companyState(CompanyState.COMBIFILE_APPROVED)
                                                .eventType(EventType.UPDATE).build());

                return result;
        }

}
