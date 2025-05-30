package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.domaine.model.Pharmacie;

import java.util.List;

public interface IServicePharmacie {
    void ajouterPharmacie(Pharmacie p);

    void supprimerPharmacie(Pharmacie p);

    List<Pharmacie> listerPharmacies();

    List<Pharmacie> rechercherPharmaciesParNomMedicament(String nomMedoc);

    // 🔍 Rechercher les pharmacies qui vendent un médicament donné
    List<Pharmacie> rechercherPharmaciesParNomMedicament(String nomMedoc, List<Pharmacie> pharmacies);

    List<Medicament> rechercherParCategorie(Pharmacie pharmacie, String categorie);
}
