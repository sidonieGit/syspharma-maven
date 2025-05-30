package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.time.LocalDate;
import java.util.List;

public interface IServiceCommande {
    Commande passerCommande(Client client, Pharmacie pharmacie);

    void annulerCommande(Commande commande);

    void confirmerLivraison(Commande commande);

    List<Commande> listerParClient(Client client);

    List<Commande> listerParPharmacie(List<Commande> commandes, Pharmacie pharmacie);

    Commande consulterParDate(Client client, LocalDate date);
}
