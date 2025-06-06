package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Pour l'injection du DataSource au constructeur
import org.springframework.stereotype.Repository; // Annotation Spring pour le bean DAO

import javax.sql.DataSource; // Importer DataSource
import java.util.List;
import java.util.Optional;

@Repository // Indique à Spring que c'est un bean DAO
public class ClientDAOImpl extends AbstractDAO implements IClientDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClientDAOImpl.class);

    // RowMapper spécifique pour l'entité Client (inchangé)
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
        return client;
    };

    /**
     * Constructeur appelé par Spring pour injecter le DataSource.
     * @param dataSource Le DataSource configuré par Spring.
     */
    @Autowired // Spring va chercher un bean de type DataSource et l'injecter ici.
    public ClientDAOImpl(DataSource dataSource) {
        super(dataSource); // Appelle le constructeur de AbstractDAO avec le DataSource
        logger.info("ClientDAOImpl bean créé et DataSource injecté.");
    }

    @Override
    public boolean inserer(Client client) throws DatabaseException {
        String sql = "INSERT INTO client (nom, prenom, email, adresse, tel, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        logger.debug("Insertion du client: " + client.getEmail());

        long generatedIdOrAffectedRows = executeUpdate(sql, stmt -> { // executeUpdate utilisera le dataSource de AbstractDAO
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getAdresse());
            stmt.setString(5, client.getTelephone());
            stmt.setString(6, client.getPassword());
            stmt.setString(7, client.getCompte().getRole().name());
        }, true); // true pour récupérer les clés générées

        if (generatedIdOrAffectedRows > 0) {
            if (client.getId() == null && generatedIdOrAffectedRows != 1) {
                client.setId(generatedIdOrAffectedRows);
                logger.info("✅ Client inséré : " + client.getEmail() + ", ID généré : " + client.getId());
            } else {
                logger.info("✅ Client inséré (ou opération réussie) pour : " + client.getEmail() + ", lignes affectées/ID: " + generatedIdOrAffectedRows);
            }
            return true;
        }
        logger.warn("Client non inséré pour : " + client.getEmail());
        return false;
    }

    @Override
    public Optional<Client> trouverParEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM client WHERE email = ?";
        logger.debug("Recherche du client par email: " + email);
        return executeQueryForSingleResult(sql, stmt -> stmt.setString(1, email), clientRowMapper);
    }

    @Override
    public List<Client> listerTous() throws DatabaseException {
        String sql = "SELECT * FROM client";
        logger.debug("Listage de tous les clients.");
        return executeQueryForList(sql, null, clientRowMapper);
    }
}