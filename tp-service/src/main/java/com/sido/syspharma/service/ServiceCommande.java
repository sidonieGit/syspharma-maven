package com.sido.syspharma.service;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.commande.Panier;
import com.sido.syspharma.domaine.enums.StatutCommande;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.service.interfaces.IServiceCommande;
import com.sido.syspharma.domaine.model.Pharmacie;


import java.time.LocalDate;
import java.util.List;

public class ServiceCommande implements IServiceCommande {

    @Override
    public Commande passerCommande(Client client, Pharmacie pharmacie) {
        Panier panier = client.getPanier();
        panier.valider();
        Commande commande = new Commande(client, panier, pharmacie);
        client.ajouterCommande(commande);
        return commande;
    }

    @Override
    public void annulerCommande(Commande commande) {
        commande.setStatut(StatutCommande.ANNULEE);
    }

    @Override
    public void confirmerLivraison(Commande commande) {
        commande.setStatut(StatutCommande.LIVREE);
    }

    @Override
    public List<Commande> listerParClient(Client client) {
        return client.getCommandes();
    }

    @Override
    public List<Commande> listerParPharmacie(List<Commande> commandes, Pharmacie pharmacie) {
        return commandes.stream()
                .filter(c -> c.getPharmacie().equals(pharmacie))
                .toList();
    }

    @Override
    public Commande consulterParDate(Client client, LocalDate date) {
        return client.getCommandes().stream()
                .filter(c -> c.getDateCommande().equals(date))
                .findFirst().orElse(null);
    }
}
