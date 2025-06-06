package com.sido.syspharma.service;

import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.service.interfaces.IServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/**
 * Service métier lié aux clients : création, connexion, récupération.
 */
public class ServiceClient implements IServiceClient {

    private final IClientDAO clientDAO;
    private static final Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    @Autowired
    public ServiceClient(IClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    /**
     * ✅ Crée un compte client si l'email n'existe pas déjà.
     */
    @Override
    public boolean creerCompte(Client client) throws BusinessException {
        logger.debug("Validation et tentative de création de compte pour l'email : {}", (client != null ? client.getEmail() : "null client"));

        if (client == null) {
            logger.warn("Tentative de création de compte avec un objet Client null.");
            throw new BusinessException("Les informations du client ne peuvent pas être nulles."); // Message spécifique pour ce cas
        }
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            logger.warn("Tentative de création de compte avec nom vide. Email: {}", client.getEmail());
            throw new BusinessException("Le nom du client ne peut pas être vide."); // Message spécifique
        }
        if (client.getEmail() == null || !client.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            logger.warn("Tentative de création de compte avec email invalide: {}", client.getEmail());
            throw new BusinessException("Format d'email invalide pour : " + client.getEmail()); // Message spécifique
        }
        if (client.getPassword() == null || client.getPassword().isEmpty()) {
            logger.warn("Tentative de création de compte avec mot de passe vide. Email: {}", client.getEmail());
            throw new BusinessException("Le mot de passe ne peut pas être vide."); // Message pour mot de passe vide
        }
        // AJOUT DE LA VÉRIFICATION DE LONGUEUR DU MOT DE PASSE ICI
        if (client.getPassword().length() < 6) {
            logger.warn("Tentative de création de compte avec mot de passe trop court. Email: {}", client.getEmail());
            throw new BusinessException("Le mot de passe doit contenir au moins 6 caractères."); // Message spécifique
        }
        if (client.getCompte() == null || client.getCompte().getRole() == null) { // Vérification du rôle
            logger.warn("Tentative de création de compte sans rôle défini. Email: {}", client.getEmail());
            throw new BusinessException("Le rôle du client doit être défini pour la création du compte.");
        }


        try {
            Optional<Client> clientExistant = clientDAO.trouverParEmail(client.getEmail());
            if (clientExistant.isPresent()) {
                logger.warn("Tentative de création de compte avec un email déjà existant: {}", client.getEmail());
                throw new BusinessException("Un compte avec l'email '" + client.getEmail() + "' existe déjà.");
            }

            return clientDAO.inserer(client);

        } catch (DatabaseException e) {
            logger.error("Erreur DAO lors de la création du compte pour {}: {}", client.getEmail(), e.getMessage(), e);
            throw new BusinessException("Une erreur technique est survenue lors de la création de votre compte. Veuillez réessayer plus tard.", e);
        }
    }

    /**
     * 🔐 Vérifie les identifiants du client.
     */
    @Override
    public boolean seConnecter(String email, String motDePasse) throws BusinessException {
        logger.info("🔐 Tentative de connexion : " + email);
        try {
            Optional<Client> optional = clientDAO.trouverParEmail(email);
            return optional
                    .filter(c -> c.getPassword().equals(motDePasse))
                    .isPresent();
        } catch (DatabaseException e) {
            logger.error("❌ Erreur DAO lors de la connexion", e);
            throw new BusinessException("Connexion impossible", e);
        }
    }

    /**
     * 📋 Récupère tous les clients.
     */
    @Override
    public List<Client> getTousLesClients() throws BusinessException {
        logger.info("📋 Récupération de tous les clients...");
        try {
            return clientDAO.listerTous();
        } catch (DatabaseException e) {
            logger.error("❌ Erreur DAO lors du listage des clients", e);
            throw new BusinessException("Erreur de récupération des clients", e);
        }
    }
}
