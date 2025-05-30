package com.sido.syspharma.service;

import com.sido.syspharma.domaine.commande.ArticlePanier;
import com.sido.syspharma.domaine.commande.Panier;
import com.sido.syspharma.domaine.model.Medicament;

public class ServicePanier implements com.sido.syspharma.service.interfaces.IServicePanier {

    @Override
    public void ajouterArticle(Panier panier, Medicament medicament, int quantite) {
        panier.ajouterArticle(new ArticlePanier(medicament, quantite));
    }

    @Override
    public void supprimerArticle(Panier panier, Medicament medicament) {
        panier.supprimerArticle(medicament);
    }

    @Override
    public void viderPanier(Panier panier) {
        panier.vider();
    }

    @Override
    public void incrementerQuantite(Panier panier, Medicament medicament) {
        panier.incrementerQuantite(medicament);
    }

    @Override
    public void decrementerQuantite(Panier panier, Medicament medicament) {
        panier.decrementerQuantite(medicament);
    }

    @Override
    public double calculerMontant(Panier panier) {
        return panier.calculerMontantTotal();
    }
}
