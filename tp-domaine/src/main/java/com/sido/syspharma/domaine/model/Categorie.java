package com.sido.syspharma.domaine.model;

public class Categorie {
    String designation;

    public Categorie(String designation) {
        this.designation = designation;
    }
    // getter
    public String getDesignation() {
        return designation;
    }
   //setter
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "designation='" + designation + '\'' +
                '}';
    }
}
