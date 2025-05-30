package com.sido.syspharma.domaine.commande;

import com.sido.syspharma.domaine.enums.EtatPanier;
import com.sido.syspharma.domaine.model.Medicament;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Panier {
    private List<ArticlePanier> articles;
    private EtatPanier etat;

    public Panier() {
        this.articles = new ArrayList<>();
        this.etat = EtatPanier.EN_COURS;
    }

    public void ajouterArticle(ArticlePanier article) {
        for (ArticlePanier a : articles) {
            if (a.getMedicament().equals(article.getMedicament())) {
                a.setQuantite(a.getQuantite() + article.getQuantite());
                return;
            }
        }
        articles.add(article);
    }

    public void supprimerArticle(Medicament medicament) {
        articles.removeIf(a -> a.getMedicament().equals(medicament));
    }

    public void vider() {
        articles.clear();
        etat = EtatPanier.VIDE;
    }

    public double calculerMontantTotal() {
        return articles.stream().mapToDouble(ArticlePanier::getPrixTotal).sum();
    }

    public List<ArticlePanier> getArticles() {
        return articles;
    }

    public EtatPanier getEtat() {
        return etat;
    }

    public void valider() {
        this.etat = EtatPanier.VALIDE;
    }

    // ✅ Ajout : incrémenter la quantité d’un médicament existant
    public void incrementerQuantite(Medicament medicament) {
        for (ArticlePanier article : articles) {
            if (article.getMedicament().equals(medicament)) {
                article.setQuantite(article.getQuantite() + 1);
                return;
            }
        }
    }

    // ✅ Ajout : décrémenter la quantité ou supprimer si 0
    public void decrementerQuantite(Medicament medicament) {
        Iterator<ArticlePanier> iterator = articles.iterator();
        while (iterator.hasNext()) {
            ArticlePanier article = iterator.next();
            if (article.getMedicament().equals(medicament)) {
                int nouvelleQuantite = article.getQuantite() - 1;
                if (nouvelleQuantite <= 0) {
                    iterator.remove(); // supprime l’article
                } else {
                    article.setQuantite(nouvelleQuantite);
                }
                return;
            }
        }
    }
}
