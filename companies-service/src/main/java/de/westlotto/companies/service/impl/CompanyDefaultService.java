package de.westlotto.companies.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.westlotto.companies.event.CompanyEvent;
import de.westlotto.companies.event.EventType;
import de.westlotto.companies.model.Company;
import de.westlotto.companies.repository.CompanyRepository;
import de.westlotto.companies.service.CompanyService;

@Service
@Transactional
public class CompanyDefaultService implements CompanyService {
    Logger log = LoggerFactory.getLogger(CompanyDefaultService.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CompanyRepository companyRepository;

    private Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company createCompany(Company company) {
        if (company.getId() != null) {
            throw new IllegalArgumentException(String.format("Company already has an id: %d", company.getId()));
        }
        Company result = this.save(company);
        applicationEventPublisher
                .publishEvent(CompanyEvent.builder().company(result).eventType(EventType.CREATE).build());
        return result;
    }

    @Override
    public Company updateCompany(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            throw new IllegalArgumentException(String.format("Company with id %d does not exist", company.getId()));
        }
        Company result = this.save(company);
        applicationEventPublisher
                .publishEvent(CompanyEvent.builder().company(company).eventType(EventType.UPDATE).build());
        return result;
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Iterable<Company> findAll() {
        return companyRepository.findAll();
    }

}
