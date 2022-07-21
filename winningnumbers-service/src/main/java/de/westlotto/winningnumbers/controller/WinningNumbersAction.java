package de.westlotto.winningnumbers.controller;

import lombok.Data;

@Data
public class WinningNumbersAction {
    private Type type;

    public static enum Type {
        READY_TO_RECEIVE,
        APPROVE_WINNINGNUMBERS
    }
}
