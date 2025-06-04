package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.ICompteDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.enums.StatutCompte;
import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import org.apache.log4j.Logger;
import java.util.Optional;

/**
 * DAO pour la gestion des comptes utilisateur.
 */
public class CompteDAOImpl extends AbstractDAO implements ICompteDAO {

    private static final Logger logger = Logger.getLogger(CompteDAOImpl.class);

    private final RowMapper<Compte> compteRowMapper = rs -> {
        Compte compte = new Compte(
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
        compte.setStatut(StatutCompte.valueOf(rs.getString("statut")));
        return compte;
    };

    @Override
    public Compte findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM compte WHERE email = ?";

        Optional<Compte> compteOptional = executeQueryForSingleResult(sql, stmt -> stmt.setString(1, email), compteRowMapper);

        return compteOptional.orElse(null); // Retourner null si aucun compte trouvé
    }

    @Override
    public boolean update(Compte compte) throws DatabaseException {
        String sql = "UPDATE compte SET password = ?, role = ?, statut = ? WHERE email = ?";

        long rows = executeUpdate(sql, stmt -> {
            stmt.setString(1, compte.getMotDePasse());
            stmt.setString(2, compte.getRole().name());
            stmt.setString(3, compte.getStatut().name());
            stmt.setString(4, compte.getIdentifiant());
        }, false);

        return rows > 0;
    }
}