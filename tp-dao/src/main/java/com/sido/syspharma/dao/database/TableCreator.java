package com.sido.syspharma.dao.database;

import com.sido.syspharma.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    // Suppression de la logique d'initialisation de la base de données
    // La méthode initializeDatabase() est retirée.
    // Vous devez vous assurer que la base de données 'syspharma_dev' existe manuellement.

    /**
     * Crée la table 'client' si elle n'existe pas.
     * @throws DatabaseException
     */
    public static void createTableClientIfNotExists() throws DatabaseException {
        try (Connection conn = ConnexionDB.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS client (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nom VARCHAR(255) NOT NULL," +
                    "prenom VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) UNIQUE NOT NULL," + // Email unique
                    "adresse VARCHAR(255)," +
                    "tel VARCHAR(20)," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50) NOT NULL DEFAULT 'CLIENT'" + // Role par défaut
                    ")";
            stmt.execute(sql);
            System.out.println("Table 'client' vérifiée/créée avec succès.");
        } catch (SQLException e) {
            throw new DatabaseException("❌ Échec de la création/vérification de la table 'client'", e);
        }
    }

    /**
     * Crée la table 'medicament' si elle n'existe pas.
     * @throws DatabaseException
     */
    public static void createTableMedicamentIfNotExists() throws DatabaseException {
        try (Connection conn = ConnexionDB.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS medicament (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nom VARCHAR(255) NOT NULL," +
                    "description TEXT," +
                    "prix DOUBLE NOT NULL," +
                    "stock INT NOT NULL," +
                    "date_expiration DATE" +
                    ")";
            stmt.execute(sql);
            System.out.println("Table 'medicament' vérifiée/créée avec succès.");
        } catch (SQLException e) {
            throw new DatabaseException("❌ Échec de la création/vérification de la table 'medicament'", e);
        }
    }

    // Vous pouvez ajouter d'autres méthodes createTable...IfNotExists() pour d'autres tables si nécessaire
}