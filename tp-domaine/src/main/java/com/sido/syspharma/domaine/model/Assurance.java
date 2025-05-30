package com.sido.syspharma.domaine.model;

public class Assurance {
    private String numeroAssurance;
    private String designation;
    private String description;

    public Assurance(String numeroAssurance, String designation, String description) {
        this.numeroAssurance = numeroAssurance;
        this.designation = designation;
        this.description = description;
    }

    // getter/setter

    public String getNumeroAssurance() {
        return numeroAssurance;
    }

    public void setNumeroAssurance(String numeroAssurance) {
        this.numeroAssurance = numeroAssurance;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "numeroAssurance='" + numeroAssurance + '\'' +
                ", designation='" + designation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
