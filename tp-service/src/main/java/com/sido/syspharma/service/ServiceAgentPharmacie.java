package com.sido.syspharma.service;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.enums.StatutCommande;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;
import com.sido.syspharma.service.interfaces.IServiceAgentPharmacie;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service lié aux actions de l’agent de pharmacie : gestion des stocks et assurances.
 */
public class ServiceAgentPharmacie implements IServiceAgentPharmacie {

    // ---------- Gestion des médicaments ----------

    @Override
    public void ajouterMedicament(Pharmacie pharmacie, Medicament medicament) {
        pharmacie.getStock().add(medicament);
    }

    @Override
    public void supprimerMedicament(Pharmacie pharmacie, Medicament medicament) {
        pharmacie.getStock().remove(medicament);
    }

    @Override
    public void modifierMedicament(Medicament medicament, String nouvelleDescription) {
        medicament.setDescription(nouvelleDescription);
    }

    @Override
    public List<Medicament> listerMedicaments(Pharmacie pharmacie) {
        return pharmacie.getStock();
    }



    // ---------- Interaction avec les services de commande ----------


    /**
     * Lister les clients ayant passé une commande dans une pharmacie,
     * avec ou sans filtre de statut.
     */
    @Override
    public Set<Client> listerClientsAyantCommandeDansPharmacie(List<Commande> commandes, Pharmacie pharmacie, StatutCommande statut) {
        return commandes.stream()
                .filter(c -> c.getPharmacie().equals(pharmacie))
                .filter(c -> statut == null || c.getStatut().equals(statut))
                .map(Commande::getClient)
                .collect(Collectors.toSet());
    }

    /**
     * Vérifie dans quelle pharmacie une commande a été enregistrée.
     */
    @Override
    public Pharmacie verifierPharmacieCommande(Client client, String numeroCommande) {
        return client.getCommandes().stream()
                .filter(c -> c.getNumeroCommande().equals(numeroCommande))
                .map(Commande::getPharmacie)
                .findFirst()
                .orElse(null);
    }
}
