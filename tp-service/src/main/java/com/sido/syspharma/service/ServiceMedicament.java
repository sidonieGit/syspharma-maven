package com.sido.syspharma.service;

import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;
import com.sido.syspharma.domaine.exceptions.BusinessException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de gestion des médicaments (CRUD + recherche).
 */
public class ServiceMedicament implements com.sido.syspharma.service.interfaces.IServiceMedicament {

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

    @Override
    public Medicament rechercherMedicamentParNom(String nom, List<Pharmacie> pharmacies) throws BusinessException {
        return pharmacies.stream()
                .flatMap(p -> p.getStock().stream())
                .filter(m -> m.getDesignation().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Aucun médicament trouvé avec ce nom."));
    }

    @Override
    public List<Medicament> rechercherParCategorie(Pharmacie pharmacie, String categorie) {
        return pharmacie.getStock().stream()
                .filter(m -> m.getCategorie().getDesignation().equalsIgnoreCase(categorie))
                .collect(Collectors.toList());
    }
}
