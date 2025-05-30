package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.exceptions.DatabaseException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation DAO pour les opérations liées aux clients.
 */
public class ClientDAOImpl extends AbstractDAO implements IClientDAO {

    private static final Logger logger = Logger.getLogger(ClientDAOImpl.class);

    public ClientDAOImpl() {
        // Constructeur par défaut
    }

    /**
     * 🔄 Insertion d’un nouveau client dans la base de données.
     */
    @Override
    public boolean inserer(Client client) throws DatabaseException {
        String sql = "INSERT INTO client (nom, prenom, email, adresse, tel, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getAdresse());
            stmt.setString(5, client.getTelephone());
            stmt.setString(6, client.getPassword());
            stmt.setString(7, client.getCompte().getRole().name());

            int rows = stmt.executeUpdate();

            logger.info("✅ Client inséré avec succès : " + client.getEmail());
            return rows > 0;

        } catch (SQLException e) {
            //logger.error("❌ Erreur lors de l'insertion du client : " + client.getEmail(), e);
            logger.error("❌ Erreur SQL détaillée lors de l'insertion du client " + client.getEmail() +
                    ": " + e.getSQLState() + " - " + e.getMessage(), e);
            throw new DatabaseException("❌ Insertion client échouée en raison d'une erreur base de données.", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * 🔍 Recherche un client par son email (utile pour la connexion).
     */
    @Override
    public Optional<Client> trouverParEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM client WHERE email = ?";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Role roleFromDb = Role.valueOf(rs.getString("role")); // Récupérer le rôle de la DB
                Client client = new Client( // Dans trouverParEmail et listerTous
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getString("tel"), // Lire la colonne 'tel'
                        rs.getString("password"),
                        roleFromDb                 // Passer le rôle lu
                );
// Si Client a besoin d'un numeroCommande lors de sa création ou s'il est optionnel:
                // client.setNumeroCommande(rs.getString("numero_commande")); // Si une telle colonne existe

                return Optional.of(client);
            }

        } catch (SQLException e) {
            logger.error("❌ Erreur lors de la recherche du client avec email : " + email, e);
            throw new DatabaseException("Erreur recherche client", e);
        } finally {
            closeConnection();
        }

        return Optional.empty();
    }

    /**
     * 🔁 Liste tous les clients présents dans la base.
     */
    @Override
    public List<Client> listerTous() throws DatabaseException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Role roleFromDb = Role.valueOf(rs.getString("role")); // Récupérer le rôle
                Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("adresse"),
                        rs.getString("tel"),       // Lire la colonne 'tel'
                        rs.getString("password"),
                        roleFromDb                 // Passer le rôle lu
                );
                // client.setNumeroCommande(rs.getString("numero_commande")); // Si applicable
                clients.add(client);
            }

        } catch (SQLException e) {
            logger.error("❌ Erreur lors du listage des clients", e);
            throw new DatabaseException("Erreur listage clients", e);
        } finally {
            closeConnection();
        }

        return clients;
    }
}
