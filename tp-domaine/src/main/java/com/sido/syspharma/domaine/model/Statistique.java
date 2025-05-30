package com.sido.syspharma.domaine.model;

import java.util.Map;

/**
 * Représente un ensemble de données statistiques pour une période donnée.
 * Générée à partir des commandes d'une ou plusieurs pharmacies.
 */
public class Statistique {
    private int nombreCommandes;
    private int nombreClients;
    private double chiffreAffaire;
    private int nombreProduitsVendus;

    // Médicaments les plus commandés : clé = Médicament, valeur = quantité totale commandée
    private Map<Medicament, Integer> medicamentsLesPlusCommandes;

    public Statistique(int nombreCommandes, int nombreClients, double chiffreAffaire,
                       int nombreProduitsVendus, Map<Medicament, Integer> medicamentsLesPlusCommandes) {
        this.nombreCommandes = nombreCommandes;
        this.nombreClients = nombreClients;
        this.chiffreAffaire = chiffreAffaire;
        this.nombreProduitsVendus = nombreProduitsVendus;
        this.medicamentsLesPlusCommandes = medicamentsLesPlusCommandes;
    }

    public int getNombreCommandes() {
        return nombreCommandes;
    }

    public int getNombreClients() {
        return nombreClients;
    }

    public double getChiffreAffaire() {
        return chiffreAffaire;
    }

    public int getNombreProduitsVendus() {
        return nombreProduitsVendus;
    }

    public Map<Medicament, Integer> getMedicamentsLesPlusCommandes() {
        return medicamentsLesPlusCommandes;
    }

    /**
     * Affiche un résumé textuel des statistiques.
     */
    public void afficher() {
        System.out.println("📊 Statistiques :");
        System.out.println("- Nombre de commandes : " + nombreCommandes);
        System.out.println("- Nombre de clients : " + nombreClients);
        System.out.println("- Nombre total de produits vendus : " + nombreProduitsVendus);
        System.out.println("- Chiffre d'affaires : " + chiffreAffaire + " FCFA");
        System.out.println("- Médicaments les plus commandés :");

        if (medicamentsLesPlusCommandes != null && !medicamentsLesPlusCommandes.isEmpty()) {
            medicamentsLesPlusCommandes.forEach((medoc, quantite) ->
                    System.out.println("  • " + medoc.getDesignation() + " : " + quantite + " unités"));
        } else {
            System.out.println("  Aucune commande enregistrée sur cette période.");
        }
    }
}
