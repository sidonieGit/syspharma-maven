package com.sido.syspharma.exceptions;

/**
 * Exception personnalisée pour signaler les erreurs d’accès BD.
 */
public class DatabaseException extends Exception {

    // ✅ Constructeur par défaut
    public DatabaseException(Throwable cause) {
        super("ERREUR ACCES BASE DE DONNEES", cause);
    }

    // ✅ Constructeur personnalisé avec message
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "ERREUR ACCES BASE DE DONNEES: " + super.getMessage();
    }
}
