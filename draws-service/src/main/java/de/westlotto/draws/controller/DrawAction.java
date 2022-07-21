package de.westlotto.draws.controller;

import de.westlotto.draws.model.DrawState;
import lombok.Data;

@Data
public class DrawAction {
    private Type type;
    private DrawState state;

    public static enum Type {
        CHANGE_STATE
    }
}
