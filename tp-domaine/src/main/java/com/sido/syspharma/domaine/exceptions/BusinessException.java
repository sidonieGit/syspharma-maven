package com.sido.syspharma.domaine.exceptions;

/**
 * Représente une exception fonctionnelle (métier) dans la couche service.
 * À ne pas confondre avec les exceptions techniques (ex: SQLException, DataBaseException).
 */
public class BusinessException extends Exception {

    /**
     * Constructeur avec message uniquement.
     * @param message Message de l'erreur métier.
     */
    public BusinessException(String message) {
        super("ERREUR MÉTIER : " + message);
    }

    /**
     * Constructeur avec message et cause sous-jacente.
     * @param message Message explicite
     * @param cause Exception technique (ex: DAO)
     */
    public BusinessException(String message, Throwable cause) {
        super("ERREUR MÉTIER : " + message, cause);
    }
}
