package com.sido.syspharma.domaine.model;

import java.util.ArrayList;
import java.util.List;

public class Pharmacie {
    private String designation;
    private String email;
    private String adresse;
    private String telephone;
    private String directeur;
    private String heureOuverture;
    private String heureFermeture;

    // ✅ AJOUTS MANQUANTS
    private List<Assurance> assurances;
    private List<Medicament> stock;

    private AgentPharmacie agentResponsable;

    public Pharmacie(String designation, String email, String adresse, String telephone,
                     String directeur, String heureOuverture, String heureFermeture) {
        this.designation = designation;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
        this.directeur = directeur;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.assurances = new ArrayList<>();
        this.stock = new ArrayList<>();
    }

    public List<Assurance> getAssurances() {
        return assurances;
    }

    public List<Medicament> getStock() {
        return stock;
    }

    public AgentPharmacie getAgentResponsable() {
        return agentResponsable;
    }

    public void setAgentResponsable(AgentPharmacie agentResponsable) {
        this.agentResponsable = agentResponsable;
    }

    // Getters

    public String getDesignation() {
        return designation;
    }

    public String getEmail() {
        return email;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getDirecteur() {
        return directeur;
    }

    public String getHeureOuverture() {
        return heureOuverture;
    }

    public String getHeureFermeture() {
        return heureFermeture;
    }

    //  setters des autres champs si besoin

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setDirecteur(String directeur) {
        this.directeur = directeur;
    }

    public void setHeureOuverture(String heureOuverture) {
        this.heureOuverture = heureOuverture;
    }

    public void setHeureFermeture(String heureFermeture) {
        this.heureFermeture = heureFermeture;
    }

    public void setAssurances(List<Assurance> assurances) {
        this.assurances = assurances;
    }

    public void setStock(List<Medicament> stock) {
        this.stock = stock;
    }
}
