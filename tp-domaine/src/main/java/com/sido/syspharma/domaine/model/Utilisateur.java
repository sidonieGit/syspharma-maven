package com.sido.syspharma.domaine.model;

import com.sido.syspharma.domaine.enums.Role;
// Plus d'import de com.sido.syspharma.dao.interfaces.IConnexion;

/**
 * Classe de base abstraite pour les utilisateurs du système.
 */
public abstract class Utilisateur { // N'implémente plus IConnexion
    protected Long id; // AJOUT : Champ pour l'identifiant
    protected String nom;
    protected String prenom;
    protected String email;
    protected String adresse;
    protected String telephone;
    protected String password; // Potentiellement à revoir si le password doit être dans le domaine
    protected Compte compte;

    public Utilisateur(String nom, String prenom, String email, String adresse, String telephone, String password, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
        this.password = password;
        this.compte = new Compte(email, password, role); // Le rôle est utilisé pour créer l'objet Compte
    }

    // Getters
    public Long getId() { return id; } // AJOUT : Getter pour l'ID
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getPassword() { return password; } // À protéger ou retirer si le password est géré uniquement par le service
    public Compte getCompte() { return compte; }

    // Setters
    public void setId(Long id) { this.id = id; } // AJOUT : Setter pour l'ID
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setPassword(String password) { this.password = password; } // À protéger ou retirer

    public void setCompte(Compte compte) { this.compte = compte; }




    // Les méthodes seConnecter(), seDeconnecter(), miseAJourCompte(), estActif() SONT SUPPRIMÉES de cette classe.
    // Elles seront gérées par la couche Service (AuthService) et DAO (CompteDAO/UtilisateurDAO).
}