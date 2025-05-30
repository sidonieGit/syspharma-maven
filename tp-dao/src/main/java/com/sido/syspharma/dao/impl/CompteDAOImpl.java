package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.ICompteDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.enums.StatutCompte;
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // <<< AJOUTER
import org.springframework.stereotype.Repository;           // <<< AJOUTER

import javax.sql.DataSource; // <<< AJOUTER
import java.util.Optional;
// PreparedStatement, ResultSet, SQLException ne sont plus nécessaires ici directement

/**
 * DAO pour la gestion des comptes utilisateur.
 */
@Repository // <<< AJOUTER L'ANNOTATION REPOSITORY
public class CompteDAOImpl extends AbstractDAO implements ICompteDAO {

    private static final Logger logger = LoggerFactory.getLogger(CompteDAOImpl.class); // Initialisation du logger

    // Définition du RowMapper pour Compte
    private final RowMapper<Compte> compteRowMapper = rs -> {
        Compte compte = new Compte(
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
        compte.setStatut(StatutCompte.valueOf(rs.getString("statut")));
        return compte;
    };

    // AJOUTER LE CONSTRUCTEUR AVEC INJECTION DE DATASOURCE
    @Autowired
    public CompteDAOImpl(DataSource dataSource) {
        super(dataSource);
        logger.info("CompteDAOImpl bean créé et DataSource injecté.");
    }

    @Override
    public Compte findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM compte WHERE email = ?";
        logger.debug("Recherche du compte avec email: " + email);

        Optional<Compte> compteOptional = executeQueryForSingleResult(sql,
                stmt -> stmt.setString(1, email),
                compteRowMapper
        );

        if (compteOptional.isPresent()) {
            logger.info("Compte trouvé pour email: " + email);
            return compteOptional.get();
        } else {
            logger.info("Aucun compte trouvé pour email: " + email);
            return null;
        }
    }

    @Override
    public boolean update(Compte compte) throws DatabaseException {
        String sql = "UPDATE compte SET password = ?, role = ?, statut = ? WHERE email = ?";
        logger.info("Mise à jour du compte pour email: " + compte.getIdentifiant());

        long affectedRows = executeUpdate(sql, stmt -> {
            stmt.setString(1, compte.getMotDePasse());
            stmt.setString(2, compte.getRole().name());
            stmt.setString(3, compte.getStatut().name());
            stmt.setString(4, compte.getIdentifiant());
        }, false);

        if (affectedRows > 0) {
            logger.debug("✅ " + affectedRows + " ligne(s) mise(s) à jour pour le compte : " + compte.getIdentifiant());
        } else {
            logger.warn("Aucune ligne mise à jour pour le compte : " + compte.getIdentifiant() + ". Le compte existe-t-il ?");
        }
        return affectedRows > 0;
    }
}