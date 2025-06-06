package com.sido.syspharma.dao.interfaces; // Ou le package où vous voulez le placer

// Plus besoin d'importer ConnexionDB ici si DataSource est utilisé partout
// import com.sido.syspharma.dao.database.ConnexionDB;
import com.sido.syspharma.dao.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Pour l'injection
import org.springframework.jdbc.core.JdbcTemplate; // Optionnel mais courant avec Spring JDBC
import org.springframework.stereotype.Component; // Pour que AbstractDAO puisse être un bean si nécessaire, ou juste une classe de base

import javax.sql.DataSource; // API standard pour les sources de données JDBC
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe de base abstraite pour les Data Access Objects (DAO) utilisant un DataSource géré par Spring.
 * Fournit des méthodes utilitaires pour exécuter des opérations JDBC
 * en gérant l'ouverture et la fermeture de la connexion via le DataSource.
 */
public abstract class AbstractDAO {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

    // Le DataSource sera injecté par Spring.
    // Les classes DAO concrètes hériteront de ce champ et l'utiliseront implicitement via les méthodes executeXXX.
    protected final DataSource dataSource;

    // Optionnel : Utilisation de JdbcTemplate pour simplifier davantage le code JDBC.
    // Si vous utilisez JdbcTemplate, certaines méthodes executeXXX pourraient être redéfinies
    // ou vous pourriez l'utiliser directement dans les DAOs concrets.
    // protected final JdbcTemplate jdbcTemplate;

    /**
     * Constructeur pour l'injection de dépendances du DataSource.
     * Les classes DAO concrètes devront avoir un constructeur qui appelle super(dataSource).
     * @param dataSource Le DataSource configuré par Spring.
     */
    @Autowired // Peut être sur le constructeur pour indiquer à Spring comment créer ce bean (s'il l'est)
    // ou pour les classes filles qui seront des beans @Repository.
    public AbstractDAO(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource ne peut pas être null.");
        }
        this.dataSource = dataSource;
        // this.jdbcTemplate = new JdbcTemplate(dataSource); // Si vous voulez utiliser JdbcTemplate
        logger.debug("AbstractDAO initialisé avec DataSource: " + dataSource);
    }

    // --- Interfaces fonctionnelles (inchangées) ---
    @FunctionalInterface
    protected interface StatementOperation {
        void apply(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    protected interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }

    // --- Méthodes executeXXX utilisant le DataSource injecté ---

    protected long executeUpdate(String sql, StatementOperation operation, boolean retrieveGeneratedKeys) throws DatabaseException {
        // Utilise this.dataSource.getConnection() au lieu de ConnexionDB.getConnection()
        try (Connection connection = this.dataSource.getConnection(); // <<< CHANGEMENT ICI
             PreparedStatement stmt = retrieveGeneratedKeys ?
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) :
                     connection.prepareStatement(sql)) {
            operation.apply(stmt);
            int affectedRows = stmt.executeUpdate();

            if (retrieveGeneratedKeys && affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getLong(1);
                    } else {
                        logger.warn("executeUpdate avec retrieveGeneratedKeys n'a pas retourné d'ID, mais " + affectedRows + " lignes affectées. SQL: " + sql);
                        return affectedRows;
                    }
                }
            }
            return affectedRows;
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de l'exécution de la mise à jour: " + sql, e);
            // Il est courant que Spring traduise les SQLExceptions en exceptions Runtime (DataAccessException)
            // Si vous utilisez spring-boot-starter-jdbc, vous pourriez avoir accès à cette traduction.
            // Pour l'instant, on garde DatabaseException.
            throw new DatabaseException("Erreur lors de l'exécution de la mise à jour: " + e.getMessage(), e);
        }
    }

    protected <R> Optional<R> executeQueryForSingleResult(String sql, StatementOperation paramsSetter, RowMapper<R> rowMapper) throws DatabaseException {
        try (Connection connection = this.dataSource.getConnection(); // <<< CHANGEMENT ICI
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            paramsSetter.apply(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rowMapper.mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de l'exécution de la requête (single result): " + sql, e);
            throw new DatabaseException("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
    }

    protected <R> List<R> executeQueryForList(String sql, StatementOperation paramsSetter, RowMapper<R> rowMapper) throws DatabaseException {
        List<R> results = new ArrayList<>();
        try (Connection connection = this.dataSource.getConnection(); // <<< CHANGEMENT ICI
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (paramsSetter != null) {
                paramsSetter.apply(stmt);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rowMapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur SQL lors de l'exécution de la requête (list result): " + sql, e);
            throw new DatabaseException("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
        return results;
    }
}