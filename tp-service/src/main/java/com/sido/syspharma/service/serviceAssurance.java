package com.sido.syspharma.service;

import com.sido.syspharma.domaine.model.Assurance;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.util.List;

/**
 * Service métier lié à la gestion des assurances.
 */

public class serviceAssurance implements com.sido.syspharma.service.interfaces.IserviceAssurance {
    // 🔍 Rechercher une assurance dans une pharmacie par son nom
    @Override
    public Assurance rechercherAssuranceParNomEtPharmacie(String nomAssurance, Pharmacie pharmacie) {
        return pharmacie.getAssurances().stream()
                .filter(a -> a.getDesignation().equalsIgnoreCase(nomAssurance))
                .findFirst()
                .orElse(null);
    }

    // 🔍 Rechercher une assurance par nom uniquement
    @Override
    public Assurance rechercherAssuranceParNom(String nomAssurance, List<Pharmacie> pharmacies) {
        for (Pharmacie pharmacie : pharmacies) {
            for (Assurance assurance : pharmacie.getAssurances()) {
                if (assurance.getDesignation().equalsIgnoreCase(nomAssurance)) {
                    return assurance;
                }
            }
        }
        return null;
    }

    @Override
    public void ajouterAssurance(Pharmacie pharmacie, Assurance assurance) {
        pharmacie.getAssurances().add(assurance);
    }

    @Override
    public void supprimerAssurance(Pharmacie pharmacie, Assurance assurance) {
        pharmacie.getAssurances().remove(assurance);
    }

    @Override
    public List<Assurance> listerAssurances(Pharmacie pharmacie) {
        return pharmacie.getAssurances();
    }

}
