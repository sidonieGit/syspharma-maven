package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;
import com.sido.syspharma.domaine.exceptions.BusinessException;

import java.util.List;

public interface IServiceMedicament {
    void ajouterMedicament(Pharmacie pharmacie, Medicament medicament);

    void supprimerMedicament(Pharmacie pharmacie, Medicament medicament);

    void modifierMedicament(Medicament medicament, String nouvelleDescription);

    List<Medicament> listerMedicaments(Pharmacie pharmacie);

    Medicament rechercherMedicamentParNom(String nom, List<Pharmacie> pharmacies) throws BusinessException;

    List<Medicament> rechercherParCategorie(Pharmacie pharmacie, String categorie);
}
