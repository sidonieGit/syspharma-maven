package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IMedicamentDAO;
import com.sido.syspharma.domaine.model.Categorie;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.exceptions.DatabaseException;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation des opérations CRUD pour les médicaments.
 */
public class MedicamentDAOImpl extends AbstractDAO implements IMedicamentDAO {

    private static final Logger logger = Logger.getLogger(MedicamentDAOImpl.class);

    @Override
    public boolean insererMedicament(Medicament medicament) throws DatabaseException {
        String sql = "INSERT INTO medicament (designation, prix, description, image, categorie) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            logger.info("🔄 Insertion d'un médicament : " + medicament.getDesignation());

            stmt.setString(1, medicament.getDesignation());
            stmt.setDouble(2, medicament.getPrix());
            stmt.setString(3, medicament.getDescription());
            stmt.setString(4, medicament.getImage());
            stmt.setString(5, medicament.getCategorie().getDesignation());

            int lignes = stmt.executeUpdate();

            logger.debug("✅ " + lignes + " ligne(s) insérée(s).");
            return lignes > 0;

        } catch (SQLException e) {
            logger.error("❌ Erreur lors de l'insertion en base : " + e.getMessage());
            throw new DatabaseException("Erreur lors de l'insertion du médicament", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public List<Medicament> rechercherParNom(String nom) throws DatabaseException {
        List<Medicament> resultats = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE designation LIKE ?";

        try (PreparedStatement stmt = prepareStatement(sql)) {
            logger.info("🔎 Recherche des médicaments contenant : " + nom);

            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medicament m = new Medicament(
                        rs.getString("designation"),
                        rs.getDouble("prix"),
                        rs.getString("description"),
                        rs.getString("image"),
                        new Categorie(rs.getString("categorie"))
                );
                resultats.add(m);
            }

            logger.debug("🔍 " + resultats.size() + " médicament(s) trouvé(s).");

        } catch (SQLException e) {
            logger.error("❌ Erreur lors de la recherche SQL : " + e.getMessage());
            throw new DatabaseException("Erreur lors de la recherche de médicament", e);
        } finally {
            closeConnection();
        }

        return resultats;
    }
}
