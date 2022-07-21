package de.westlotto.draws.controller;

import lombok.Data;

@Data
public class DrawCompanyAction {
    private Type type;
    private boolean readiness;

    public static enum Type {
        SUBMIT_READINESS,
    }
}
