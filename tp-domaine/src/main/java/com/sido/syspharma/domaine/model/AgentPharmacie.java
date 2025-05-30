package com.sido.syspharma.domaine.model;

import com.sido.syspharma.domaine.enums.Role;

/**
 * Représente un agent de pharmacie en charge des stocks et des commandes.
 */
public class AgentPharmacie extends Utilisateur {
    private String matricule;
    private Pharmacie pharmacie;

    public AgentPharmacie(String nom, String prenom, String email, String adresse, String telephone, String password, String matricule) {
        super(nom, prenom, email, adresse, telephone, password, Role.AGENT);
        this.matricule = matricule;
    }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public Pharmacie getPharmacie() { return pharmacie; }
    public void setPharmacie(Pharmacie pharmacie) { this.pharmacie = pharmacie; }
}
