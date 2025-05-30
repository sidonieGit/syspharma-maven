package com.sido.syspharma.domaine.util;

public class ValidateurFormulaire {

    public static boolean emailValide(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean motDePasseValide(String motDePasse) {
        return motDePasse != null && motDePasse.length() >= 6;
    }

    public static boolean telephoneValide(String tel) {
        return tel != null && tel.matches("\\d+");
    }
}
