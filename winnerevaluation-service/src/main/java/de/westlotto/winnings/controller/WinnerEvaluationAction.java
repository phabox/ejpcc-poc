package de.westlotto.winnings.controller;

import lombok.Data;

@Data
public class WinnerEvaluationAction {
    private Type type;

    public static enum Type {
        RUN_WINNER_EVALUATION,
        CONFIRM_WINNER_EVALUATION
    }
}
