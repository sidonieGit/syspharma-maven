package com.sido.syspharma.domaine.commande;

import com.sido.syspharma.domaine.enums.ModePaiement;

import java.time.LocalDate;

public class Paiement {
    private double montant;
    private ModePaiement modePaiement;
    private LocalDate datePaiement;
    private Commande commande;

    public Paiement(double montant, ModePaiement modePaiement, Commande commande) {
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.datePaiement = LocalDate.now();
        this.commande = commande;
    }

    // Getters
    public double getMontant() {
        return montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public Commande getCommande() {
        return commande;
    }

    // Setters éventuels si nécessaires
}
