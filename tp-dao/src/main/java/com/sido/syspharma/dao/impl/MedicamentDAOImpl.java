package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IMedicamentDAO;
import com.sido.syspharma.domaine.model.Categorie;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Implémentation des opérations CRUD pour les médicaments.
 */
public class MedicamentDAOImpl extends AbstractDAO implements IMedicamentDAO {

    private static final Logger logger = Logger.getLogger(MedicamentDAOImpl.class);

    // Définition du RowMapper pour les médicaments
    private final RowMapper<Medicament> medicamentRowMapper = rs -> new Medicament(
            rs.getString("designation"),
            rs.getDouble("prix"),
            rs.getString("description"),
            rs.getString("image"),
            new Categorie(rs.getString("categorie"))
    );

    @Override
    public boolean insererMedicament(Medicament medicament) throws DatabaseException {
        String sql = "INSERT INTO medicament (designation, prix, description, image, categorie) VALUES (?, ?, ?, ?, ?)";

        long rows = executeUpdate(sql, stmt -> {
            stmt.setString(1, medicament.getDesignation());
            stmt.setDouble(2, medicament.getPrix());
            stmt.setString(3, medicament.getDescription());
            stmt.setString(4, medicament.getImage());
            stmt.setString(5, medicament.getCategorie().getDesignation());
        }, false); // Pas de clés auto-générées ici

        logger.debug("✅ " + rows + " ligne(s) insérée(s).");
        return rows > 0;
    }

    @Override
    public List<Medicament> rechercherParNom(String nom) throws DatabaseException {
        String sql = "SELECT * FROM medicament WHERE designation LIKE ?";

        return executeQueryForList(sql, stmt -> {
            stmt.setString(1, "%" + nom + "%");
        }, medicamentRowMapper);
    }
}
