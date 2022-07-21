package de.westlotto.accountings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.westlotto.accountings.model.AccountingValues;
import de.westlotto.accountings.model.CompanyAccountingValues;
import de.westlotto.accountings.service.AccountingService;

@RestController
@RequestMapping("/draws")
public class AccountingController {

    @Autowired
    private AccountingService accountingService;

    @GetMapping("/{drawId}/accountings")
    public AccountingValues accountingValues(@PathVariable Long drawId) {
        try {
            return accountingService.findAccountingValues(drawId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Request could not be processed: %s", e.getMessage()));
        }
    }

    @GetMapping("/{drawId}/companies/{companyId}/accountings")
    public CompanyAccountingValues accountingValues(@PathVariable Long drawId, @PathVariable Long companyId) {
        try {
            return accountingService.findCompanyAccountingValues(drawId, companyId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Request could not be processed: %s", e.getMessage()));
        }
    }

    @PostMapping("/{drawId}/accountings/actions")
    public AccountingValues action(@PathVariable Long drawId, @RequestBody AccountingAction action) {
        switch (action.getType()) {
            case CALCULATE_ACCOUNTINGS:
                return accountingService.calculateAccountingValues(drawId);
            case CONFIRM_ACCOUNTING_CALCULATION:
                return accountingService.approve(drawId);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Unknown action transmitted"));
        }
    }
}
