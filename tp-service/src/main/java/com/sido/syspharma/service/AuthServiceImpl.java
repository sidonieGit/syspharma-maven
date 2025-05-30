package com.sido.syspharma.service;

import com.sido.syspharma.service.interfaces.IAuthService;
import com.sido.syspharma.dao.interfaces.ICompteDAO; // Vous aurez probablement besoin d'un DAO pour Compte ou Utilisateur
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.domaine.model.Utilisateur;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.exceptions.DatabaseException; // Pour capturer et re-lever

public class AuthServiceImpl implements IAuthService {

    private ICompteDAO compteDAO; // Injectez votre DAO ici

    public AuthServiceImpl(ICompteDAO compteDAO) {
        this.compteDAO = compteDAO;
    }

    @Override
    public Utilisateur authentifier(String email, String motDePasse) throws BusinessException{
        try {
            Compte compte = compteDAO.findByEmail(email); // Méthode à créer dans ICompteDAO/CompteDAOImpl
            if (compte != null && compte.getMotDePasse().equals(motDePasse)) {
                // Supposons que Compte a une référence vers Utilisateur ou que le DAO peut récupérer l'utilisateur
                // Ceci est une simplification. La vraie logique peut être plus complexe.
                return compte.getUtilisateur();
            } else {
                throw new BusinessException("Identifiants invalides.");
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Erreur de connexion à la base de données.", e);
        }
    }

    @Override
    public boolean seDeconnecter(Utilisateur user) {
        // Logique de déconnexion si nécessaire (ex: invalider une session)
        return true;
    }

    @Override
    public boolean mettreAJourCompte(Compte nouveauCompte) throws BusinessException {
        try {
            return compteDAO.update(nouveauCompte); // Méthode à créer dans ICompteDAO/CompteDAOImpl
        } catch (DatabaseException e) {
            throw new BusinessException("Impossible de mettre à jour le compte.", e);
        }
    }

    @Override
    public boolean estActif(Utilisateur user) {
        // Logique pour vérifier si un utilisateur est actif
        return user.getCompte().getStatut().isActif();
    }
}