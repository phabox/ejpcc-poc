package de.westlotto.companies.service;

import java.util.Optional;

import de.westlotto.companies.model.Company;

public interface CompanyService {
    public Company createCompany(Company company);

    public Company updateCompany(Company company);

    public Optional<Company> findById(Long id);

    public Iterable<Company> findAll();
}
