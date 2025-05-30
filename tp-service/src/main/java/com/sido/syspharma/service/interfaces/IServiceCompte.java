package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.enums.StatutCompte;
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.domaine.model.Utilisateur;

import java.util.List;

public interface IServiceCompte {
    void modifierStatutCompte(Compte compte, StatutCompte nouveauStatut);

    void desactiverCompteParIdentifiant(List<Utilisateur> utilisateurs, String email);
    /**
     * Met à jour les informations du compte (ex : mot de passe).
     * @param nouveauCompte les nouvelles données
     * @return true si la mise à jour est réussie
     */
    boolean miseAJourCompte(Compte nouveauCompte);

    /**
     * Vérifie si un compte est actif.
     * @return true si actif, false sinon
     */
    boolean estActif();

}
