package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.model.Assurance;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.util.List;

public interface IserviceAssurance {
    // 🔍 Rechercher une assurance dans une pharmacie par son nom
    Assurance rechercherAssuranceParNomEtPharmacie(String nomAssurance, Pharmacie pharmacie);

    // 🔍 Rechercher une assurance par nom uniquement
    Assurance rechercherAssuranceParNom(String nomAssurance, List<Pharmacie> pharmacies);

    void ajouterAssurance(Pharmacie pharmacie, Assurance assurance);

    void supprimerAssurance(Pharmacie pharmacie, Assurance assurance);

    List<Assurance> listerAssurances(Pharmacie pharmacie);
}
