package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.model.Client;
import org.apache.log4j.Logger;

// Statement, PreparedStatement, ResultSet, SQLException sont gérés par AbstractDAO ou implicites
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAOImpl extends AbstractDAO implements IClientDAO {

    private static final Logger logger = Logger.getLogger(ClientDAOImpl.class);

    // RowMapper spécifique pour l'entité Client
    private final RowMapper<Client> clientRowMapper = rs -> {
        Role roleFromDb = Role.valueOf(rs.getString("role"));
        Client client = new Client(
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("adresse"),
                rs.getString("tel"),
                rs.getString("password"),
                roleFromDb
        );
        client.setId(rs.getLong("id"));
        // client.setNumeroCommande(rs.getString("numero_commande")); // Si applicable
        return client;
    };

    public ClientDAOImpl() {
        // Constructeur par défaut
        // Si AbstractDAO avait un constructeur avec ConnectionProvider, on l'appellerait ici.
    }

    @Override
    public boolean inserer(Client client) throws DatabaseException {
        String sql = "INSERT INTO client (nom, prenom, email, adresse, tel, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        long generatedIdOrAffectedRows = executeUpdate(sql, stmt -> {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getAdresse());
            stmt.setString(5, client.getTelephone());
            stmt.setString(6, client.getPassword());
            stmt.setString(7, client.getCompte().getRole().name());
        }, true); // true pour récupérer les clés générées

        if (generatedIdOrAffectedRows > 0 && client.getId() == null) { // Si l'ID a été généré et retourné
            if (generatedIdOrAffectedRows != 1 ) { // Cas où generatedIdOrAffectedRows est l'ID
                client.setId(generatedIdOrAffectedRows);
                logger.info("✅ Client inséré : " + client.getEmail() + ", ID généré : " + client.getId());
            } else { // Cas où generatedIdOrAffectedRows est le nombre de lignes (1) et l'ID n'a pas été retourné par la DB (peu probable avec auto_increment)
                logger.warn("⚠️ Client inséré, mais l'ID généré n'a pas été explicitement retourné, lignes affectées: " + generatedIdOrAffectedRows);
            }
            return true;
        } else if (generatedIdOrAffectedRows > 0 && client.getId() != null) { // L'ID était déjà là (si la logique changeait) ou si l'ID a été correctement setté
            logger.info("✅ Client inséré (ou opération réussie) : " + client.getEmail() + ", lignes affectées/ID: " + generatedIdOrAffectedRows);
            return true;
        }
        logger.warn("Client non inséré, aucune ligne affectée ou ID généré pour : " + client.getEmail());
        return false;
    }


    @Override
    public Optional<Client> trouverParEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM client WHERE email = ?";
        return executeQueryForSingleResult(sql, stmt -> stmt.setString(1, email), clientRowMapper);
    }

    @Override
    public List<Client> listerTous() throws DatabaseException {
        String sql = "SELECT * FROM client";
        // Pas de paramètres pour lister tous, donc on peut passer une lambda vide ou null pour paramsSetter
        return executeQueryForList(sql, null, clientRowMapper);
        // Ou explicitement :
        // return executeQueryForList(sql, stmt -> {}, clientRowMapper);
    }
}