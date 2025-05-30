package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.domaine.model.Utilisateur;
import com.sido.syspharma.domaine.exceptions.BusinessException;// Si nécessaire

public interface IAuthService {
    Utilisateur authentifier(String email, String motDePasse) throws BusinessException;
    boolean seDeconnecter(Utilisateur user); // Peut être une méthode simple si la déconnexion est simple
    boolean mettreAJourCompte(Compte nouveauCompte) throws BusinessException;
    boolean estActif(Utilisateur user);
}