package com.sido.syspharma.service;

import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;
import com.sido.syspharma.service.interfaces.IServicePharmacie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServicePharmacie implements IServicePharmacie {

    private List<Pharmacie> pharmacies = new ArrayList<>();

    @Override
    public void ajouterPharmacie(Pharmacie p) {
        pharmacies.add(p);
    }

    @Override
    public void supprimerPharmacie(Pharmacie p) {
        pharmacies.remove(p);
    }

    @Override
    public List<Pharmacie> listerPharmacies() {
        return pharmacies;
    }

    /**
     * Rechercher toutes les pharmacies proposant un médicament.
     */
    @Override
    public List<Pharmacie> rechercherPharmaciesParNomMedicament(String nomMedoc) {
        return pharmacies.stream()
                .filter(p -> p.getStock().stream()
                        .anyMatch(m -> m.getDesignation().equalsIgnoreCase(nomMedoc)))
                .toList();
    }


    // 🔍 Rechercher les pharmacies qui vendent un médicament donné
    @Override
    public List<Pharmacie> rechercherPharmaciesParNomMedicament(String nomMedoc, List<Pharmacie> pharmacies) {
        return pharmacies.stream()
                .filter(p -> p.getStock().stream()
                        .anyMatch(m -> m.getDesignation().equalsIgnoreCase(nomMedoc)))
                .collect(Collectors.toList());
    }
    @Override
    public List<Medicament> rechercherParCategorie(Pharmacie pharmacie, String categorie) {
        return pharmacie.getStock().stream()
                .filter(m -> m.getCategorie().getDesignation().equalsIgnoreCase(categorie))
                .collect(Collectors.toList());
    }


}
