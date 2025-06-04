package com.sido.syspharma.domaine.model;

import com.sido.syspharma.domaine.enums.Role;


/**
 * Représente un administrateur ayant un rôle global sur la plateforme.
 */
public class Administrateur extends Utilisateur {

    public Administrateur(String nom, String prenom, String email, String adresse, String telephone, String password) {
        super(nom, prenom, email, adresse, telephone, password, Role.ADMINISTRATEUR);
    }

    // Tu pourras ajouter des méthodes spécifiques à la gestion plus tard
}
