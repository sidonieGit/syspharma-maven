package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.enums.StatutCommande;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.util.List;
import java.util.Set;

public interface IServiceAgentPharmacie {
    void ajouterMedicament(Pharmacie pharmacie, Medicament medicament);

    void supprimerMedicament(Pharmacie pharmacie, Medicament medicament);

    void modifierMedicament(Medicament medicament, String nouvelleDescription);

    List<Medicament> listerMedicaments(Pharmacie pharmacie);

    Set<Client> listerClientsAyantCommandeDansPharmacie(List<Commande> commandes, Pharmacie pharmacie, StatutCommande statut);

    Pharmacie verifierPharmacieCommande(Client client, String numeroCommande);
}
