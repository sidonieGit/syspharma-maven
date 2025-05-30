package com.sido.syspharma.domaine.model;

import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.enums.StatutCompte;

/**
 * Entité représentant les informations de connexion d’un utilisateur.
 */
public class Compte {

    private String identifiant; // généralement l'email
    private String motDePasse;
    private Role role;
    private StatutCompte statut;

    private Utilisateur utilisateur; // lien logique vers l'utilisateur (client, agent, admin)

    public Compte(String identifiant, String motDePasse, Role role) {
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
        this.statut = StatutCompte.ACTIF; // par défaut
    }

    // Getters & Setters
    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public StatutCompte getStatut() {
        return statut;
    }

    public void setStatut(StatutCompte statut) {
        this.statut = statut;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
