package de.westlotto.combifiles.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import de.westlotto.combifiles.model.Combifile;
import de.westlotto.combifiles.service.CombifileService;

@RestController
@RequestMapping("/draws")
public class CombifleController {

    @Autowired
    private CombifileService combifileService;

    @GetMapping("/{drawId}/companies/{companyId}/combifiles")
    public Optional<Combifile> findOne(@PathVariable("drawId") Long drawId, @PathVariable("companyId") Long companyId) {
        return combifileService.findCombifile(drawId, companyId);
    }

    @GetMapping("/{drawId}/combifiles")
    public List<Combifile> findAll(@PathVariable("drawId") Long drawId) {
        return combifileService.findCombifiles(drawId);
    }

    @PostMapping("/{drawId}/companies/{companyId}/combifiles")
    public Combifile upload(@RequestParam(value = "file", required = true) MultipartFile file,
            @PathVariable("drawId") Long drawId, @PathVariable("companyId") Long companyId) {
        try {
            return combifileService.processFileUpload(drawId, companyId, file);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Combifile could not be processed: %s", e.getMessage()));
        }
    }

    @PostMapping("/{drawId}/companies/{companyId}/combifiles/actions")
    public Combifile action(@PathVariable Long drawId, @PathVariable Long companyId,
            @RequestBody CombifileAction action) {
        switch (action.getType()) {
            case CONFIRM_METADATA:
                return combifileService.confirmMetadata(drawId, companyId, action.isMetadataConfirmed());
            case APPROVE_COMBIFILE:
                return combifileService.approveCombifile(drawId, companyId, action.isCombifileApproved());
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }

}