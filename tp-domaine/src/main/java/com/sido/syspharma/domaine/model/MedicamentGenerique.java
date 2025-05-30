package com.sido.syspharma.domaine.model;

public class MedicamentGenerique extends Medicament {
    private Medicament medicamentOriginal;

    public MedicamentGenerique(String designation, double prix, String description, String image, Categorie categorie, Medicament medicamentOriginal) {
        super(designation, prix, description, image, categorie);
        this.medicamentOriginal = medicamentOriginal;
    }

    // getter
}
