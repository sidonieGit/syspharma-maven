package com.sido.syspharma.domaine.model;

public class Generique extends Medicament {
    private Medicament medicamentOriginal;

    public Generique(String designation, double prix, String description, String image, Categorie categorie, Medicament medicamentOriginal) {
        super(designation, prix, description, image, categorie);
        this.medicamentOriginal = medicamentOriginal;
    }

    // getter

    public Medicament getMedicamentOriginal() {
        return medicamentOriginal;
    }

    public void setMedicamentOriginal(Medicament medicamentOriginal) {
        this.medicamentOriginal = medicamentOriginal;
    }

    @Override
    public String toString() {
        return "Generique{" +
                "medicamentOriginal=" + medicamentOriginal +
                '}';
    }
}
