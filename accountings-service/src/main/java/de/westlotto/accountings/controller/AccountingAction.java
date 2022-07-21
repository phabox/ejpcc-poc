package de.westlotto.accountings.controller;

import lombok.Data;

@Data
public class AccountingAction {
    private Type type;

    public static enum Type {
        CALCULATE_ACCOUNTINGS,
        CONFIRM_ACCOUNTING_CALCULATION
    }
}
