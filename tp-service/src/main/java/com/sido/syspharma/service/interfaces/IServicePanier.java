package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.commande.Panier;
import com.sido.syspharma.domaine.model.Medicament;

public interface IServicePanier {
    void ajouterArticle(Panier panier, Medicament medicament, int quantite);

    void supprimerArticle(Panier panier, Medicament medicament);

    void viderPanier(Panier panier);

    void incrementerQuantite(Panier panier, Medicament medicament);

    void decrementerQuantite(Panier panier, Medicament medicament);

    double calculerMontant(Panier panier);
}
