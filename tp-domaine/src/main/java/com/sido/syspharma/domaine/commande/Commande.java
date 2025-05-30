package com.sido.syspharma.domaine.commande;

import com.sido.syspharma.domaine.enums.StatutCommande;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.time.LocalDate;

public class Commande {
    private static int compteur = 1;

    private String numeroCommande; // formaté
    private LocalDate dateCommande;
    private StatutCommande statut;
    private double montantTotal;
    private Panier panier;
    private Client client;
    private Pharmacie pharmacie;
    private Paiement paiement;

    public Commande(Client client, Panier panier, Pharmacie pharmacie) {
        this.numeroCommande = genererNumeroCommande(compteur++);
        this.dateCommande = LocalDate.now();
        this.statut = StatutCommande.EN_ATTENTE;
        this.panier = panier;
        this.client = client;
        this.pharmacie = pharmacie;
        this.montantTotal = panier.calculerMontantTotal();
    }

    public static String genererNumeroCommande(int id) {
        return "CMD-" + LocalDate.now().getYear() + "-" + String.format("%06d", id);
    }

    // getters

    public static int getCompteur() {
        return compteur;
    }

    public String getNumeroCommande() {
        return numeroCommande;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public Panier getPanier() {
        return panier;
    }

    public Pharmacie getPharmacie() {
        return pharmacie;
    }

    public Client getClient() {
        return client;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    // setters comme avant

    public static void setCompteur(int compteur) {
        Commande.compteur = compteur;
    }

    public void setNumeroCommande(String numeroCommande) {
        this.numeroCommande = numeroCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setPharmacie(Pharmacie pharmacie) {
        this.pharmacie = pharmacie;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }
}
