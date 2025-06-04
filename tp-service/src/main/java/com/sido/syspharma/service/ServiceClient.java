package com.sido.syspharma.service;

import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.service.interfaces.IServiceClient;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Service métier lié aux clients : création, connexion, récupération.
 */
public class ServiceClient implements IServiceClient {

    private final IClientDAO clientDAO;
    private static final Logger logger = Logger.getLogger(ServiceClient.class);

    public ServiceClient(IClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    /**
     * ✅ Crée un compte client si l'email n'existe pas déjà.
     */
    @Override
    public boolean creerCompte(Client client) throws BusinessException {
        try {
            return clientDAO.inserer(client); // ou clientDAO.creer(client) etc.
        } catch (DatabaseException e) {
            // LOGUEZ LA CAUSE ICI AUSSI
            logger.error("Erreur DAO lors de la création du compte pour " + client.getEmail() + ": " +
                    e.getMessage(), e.getCause()); // e.getCause() est la SQLException
            throw new BusinessException("Erreur lors de la création du client", e);
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
