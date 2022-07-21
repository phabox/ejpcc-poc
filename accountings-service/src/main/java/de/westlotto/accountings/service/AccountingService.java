package de.westlotto.accountings.service;

import de.westlotto.accountings.model.AccountingValues;
import de.westlotto.accountings.model.CompanyAccountingValues;

public interface AccountingService {

    AccountingValues findAccountingValues(Long drawId);

    CompanyAccountingValues findCompanyAccountingValues(Long drawId, Long companyId);

    AccountingValues calculateAccountingValues(Long drawId);

    AccountingValues approve(Long drawId);
}
