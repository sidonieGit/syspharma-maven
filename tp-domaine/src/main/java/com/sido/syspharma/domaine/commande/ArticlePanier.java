package com.sido.syspharma.domaine.commande;


import com.sido.syspharma.domaine.model.Medicament;

public class ArticlePanier {
        private Medicament medicament;
        private int quantite;

        public ArticlePanier(Medicament medicament, int quantite) {
            this.medicament = medicament;
            this.quantite = quantite;

        }
        //getter
        public Medicament getMedicament() {
            return medicament;
        }

        public int getQuantite() {
            return quantite;
        }

        public double getPrixTotal() {
            return quantite * getMedicament().getPrix();
        }
        //setter

        public void setMedicament(Medicament medicament) {
            this.medicament = medicament;
        }

        public void setQuantite(int quantite) {
            this.quantite = quantite;
        }
}
