package com.sido.syspharma.domaine.enums;

public enum StatutCompte {
    ACTIF,
    INACTIF,
    DESACTIVE;

    public boolean isActif() {
        return this == ACTIF;
    }
}
