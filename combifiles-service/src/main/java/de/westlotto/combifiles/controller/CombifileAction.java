package de.westlotto.combifiles.controller;

import lombok.Data;

@Data
public class CombifileAction {
    private Type type;
    private boolean metadataConfirmed;
    private boolean combifileApproved;

    public static enum Type {
        CONFIRM_METADATA, // Company
        APPROVE_COMBIFILE // CC
    }
}
