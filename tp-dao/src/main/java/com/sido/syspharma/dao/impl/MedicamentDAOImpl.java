package com.sido.syspharma.dao.impl;

import com.sido.syspharma.dao.interfaces.AbstractDAO;
import com.sido.syspharma.dao.interfaces.IMedicamentDAO;
import com.sido.syspharma.domaine.model.Categorie;
import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // <<< AJOUTER CET IMPORT
import org.springframework.stereotype.Repository;           // <<< AJOUTER CET IMPORT

import javax.sql.DataSource; // <<< AJOUTER CET IMPORT
// java.sql.PreparedStatement, ResultSet, SQLException ne sont plus nécessaires ici directement
import java.util.List;
// import java.util.ArrayList; // Plus nécessaire si executeQueryForList est utilisé

/**
 * Implémentation des opérations CRUD pour les médicaments.
 */
@Repository // <<< AJOUTER L'ANNOTATION REPOSITORY
public class MedicamentDAOImpl extends AbstractDAO implements IMedicamentDAO {

    private static final Logger logger = LoggerFactory.getLogger(MedicamentDAOImpl.class);

    // Définition du RowMapper pour les médicaments (peut rester tel quel)
    private final RowMapper<Medicament> medicamentRowMapper = rs -> {
        // Supposons que Medicament a un setId et que la table a une colonne 'id'
        Categorie categorie = null;
        String categorieStr = rs.getString("categorie");
        if (categorieStr != null && !categorieStr.isEmpty()) {
            categorie = new Categorie(categorieStr);
        }
        Medicament medicament = new Medicament(
                rs.getString("designation"),
                rs.getDouble("prix"),
                rs.getString("description"),
                rs.getString("image"),
                categorie
        );
        // medicament.setId(rs.getLong("id")); // Décommentez si Medicament a setId et la table un 'id'
        return medicament;
    };

    // AJOUTER LE CONSTRUCTEUR AVEC INJECTION DE DATASOURCE
    @Autowired
    public MedicamentDAOImpl(DataSource dataSource) {
        super(dataSource); // Appelle le constructeur de AbstractDAO
        logger.info("MedicamentDAOImpl bean créé et DataSource injecté.");
    }

    @Override
    public boolean insererMedicament(Medicament medicament) throws DatabaseException {
        String sql = "INSERT INTO medicament (designation, prix, description, image, categorie) VALUES (?, ?, ?, ?, ?)";
        logger.info("🔄 Insertion d'un médicament : " + medicament.getDesignation());

        long affectedRows = executeUpdate(sql, stmt -> {
            stmt.setString(1, medicament.getDesignation());
            stmt.setDouble(2, medicament.getPrix());
            stmt.setString(3, medicament.getDescription());
            stmt.setString(4, medicament.getImage()); // Peut être null
            if (medicament.getCategorie() != null && medicament.getCategorie().getDesignation() != null) {
                stmt.setString(5, medicament.getCategorie().getDesignation());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR); // Gérer le cas où la catégorie ou sa désignation est null
            }
        }, false); // false car la méthode originale ne récupérait pas d'ID généré et ne le settrait pas

        if (affectedRows > 0) {
            logger.debug("✅ " + affectedRows + " ligne(s) insérée(s) pour le médicament : " + medicament.getDesignation());
        } else {
            logger.warn("Aucune ligne insérée pour le médicament : " + medicament.getDesignation());
        }
        return affectedRows > 0;
    }

    @Override
    public List<Medicament> rechercherParNom(String nom) throws DatabaseException {
        String sql = "SELECT * FROM medicament WHERE designation LIKE ?";
        logger.info("🔎 Recherche des médicaments contenant : " + nom);

        List<Medicament> resultats = executeQueryForList(sql, stmt -> {
            stmt.setString(1, "%" + nom + "%");
        }, medicamentRowMapper);

        logger.debug("🔍 " + resultats.size() + " médicament(s) trouvé(s) pour la recherche '" + nom + "'.");
        return resultats;
    }

    // Implémentez listerTous si IMedicamentDAO le définit
    // public List<Medicament> listerTous() throws DatabaseException { ... }
}