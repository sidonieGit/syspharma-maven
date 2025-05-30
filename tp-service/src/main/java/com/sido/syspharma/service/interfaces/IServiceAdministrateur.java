package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.enums.StatutCompte;
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.domaine.model.Pharmacie;
import com.sido.syspharma.domaine.model.Utilisateur;

import java.util.List;

public interface IServiceAdministrateur {
    void ajouterPharmacie(Pharmacie p);

    void supprimerPharmacie(Pharmacie p);

    void modifierPharmacie(Pharmacie p, String nouvelleAdresse);

    List<Pharmacie> listerPharmacies();

    void creerCompteAgent(String nom, String prenom, String email, String adresse, String telephone, String password, String matricule, Pharmacie pharmacie);

    List<Utilisateur> listerUtilisateurs();

    void modifierStatutCompte(Compte compte, StatutCompte nouveauStatut);

    void desactiverCompteParIdentifiant(String email);
}
