package de.westlotto.companies.controller;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.westlotto.companies.model.Company;
import de.westlotto.companies.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public Company create(@RequestBody(required = true) Company company) {
        logger.info("RECEIVED ", company);
        if (company.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Id for new company must not be provided. Argument included id %d", company.getId()));
        }
        return companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public Company update(@PathVariable Long id, @RequestBody(required = true) Company company) {
        if (!Objects.equals(id, company.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Path id %d does not match Company id %d", id, company.getId()));
        }

        return companyService.updateCompany(company);
    }

    @GetMapping("/{id}")
    public Optional<Company> one(@PathVariable("id") Long companyId) {
        logger.debug("reading company with id", companyId);
        return companyService.findById(companyId);
    }

    @GetMapping
    public Iterable<Company> all() {
        logger.debug("reading all companies");
        return companyService.findAll();
    }
}
