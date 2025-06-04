package com.sido.syspharma.service;

import com.sido.syspharma.domaine.commande.ArticlePanier;
import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Statistique;
import com.sido.syspharma.service.interfaces.IServiceStatistique;

import java.time.LocalDate;
import java.util.*;

public class ServiceStatistique implements IServiceStatistique {

    @Override
    public Statistique calculerStatistiques(List<Commande> commandes, LocalDate debut, LocalDate fin) {
        List<Commande> commandesFiltrees = commandes.stream()
                .filter(c -> !c.getDateCommande().isBefore(debut) && !c.getDateCommande().isAfter(fin))
                .toList();

        double chiffreAffaire = commandesFiltrees.stream()
                .mapToDouble(c -> c.getPanier().calculerMontantTotal())
                .sum();

        int produits = commandesFiltrees.stream()
                .flatMap(c -> c.getPanier().getArticles().stream())
                .mapToInt(ArticlePanier::getQuantite)
                .sum();

        int clients = (int) commandesFiltrees.stream()
                .map(c -> c.getClient().getEmail())
                .distinct().count();

        Map<Medicament, Integer> top = new HashMap<>();
        for (Commande c : commandesFiltrees) {
            for (ArticlePanier a : c.getPanier().getArticles()) {
                top.merge(a.getMedicament(), a.getQuantite(), Integer::sum);
            }
        }

        return new Statistique(commandesFiltrees.size(), clients, chiffreAffaire, produits, top);
    }
}
