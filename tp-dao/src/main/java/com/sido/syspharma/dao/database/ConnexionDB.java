package com.sido.syspharma.dao.database;

import com.sido.syspharma.dao.exceptions.DatabaseException; // Assurez-vous d'utiliser le bon package pour DatabaseException
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnexionDB {

    private static final String CONFIG_FILE = "db.properties"; // Assurez-vous que c'est le bon nom (db.properties ou config.properties)
    private static Properties properties = new Properties();

    private static String DB_URL_FULL; // L'URL complète incluant le nom de la BD
    private static String USER;
    private static String PASS;

    static {
        try (InputStream input = ConnexionDB.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Désolé, impossible de trouver " + CONFIG_FILE + " dans le classpath.");
                throw new RuntimeException("Fichier de configuration de base de données manquant.");
            }
            properties.load(input);

            // --- DEBUG START (pour la vérification) ---
            System.out.println("DEBUG ConnexionDB: Fichier de configuration chargé : " + CONFIG_FILE);
            System.out.println("DEBUG ConnexionDB: Contenu brut de db.url : " + properties.getProperty("db.url"));
            System.out.println("DEBUG ConnexionDB: Contenu brut de db.username : " + properties.getProperty("db.username"));
            // --- DEBUG END ---

            DB_URL_FULL = properties.getProperty("db.url"); // On prend l'URL complète directement
            USER = properties.getProperty("db.username");
            PASS = properties.getProperty("db.password");

            // Charger le driver JDBC
            // Pour les versions modernes de JDBC (>= 4.0) et JDK (>= 6), ceci est souvent automatique
            // mais le garder ne fait pas de mal si vous n'êtes pas sûr.
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception ex) {
            System.err.println("DEBUG ConnexionDB: Exception dans le bloc static d'initialisation !");
            ex.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement des propriétés de connexion à la base de données ou du driver.", ex);
        }
    }

    // La méthode createDatabaseIfNotExists() est supprimée car la BD est gérée manuellement.
    // Si vous souhaitez tout de même que les tables soient créées automatiquement dans une BD EXISTANTE,
    // la logique de création des tables resterait dans TableCreator.

    /**
     * Retourne une connexion à la base de données spécifique.
     * @return Connection
     * @throws DatabaseException
     */
    public static Connection getConnection() throws DatabaseException {
        try {
            System.out.println("DEBUG ConnexionDB: Tentative de connexion à la BD spécifique. URL: " + DB_URL_FULL + ", User: " + USER);
            return DriverManager.getConnection(DB_URL_FULL, USER, PASS);
        } catch (SQLException e) {
            System.err.println("DEBUG ConnexionDB: SQLException lors de la connexion à la BD spécifique : " + e.getMessage());
            throw new DatabaseException("❌ Échec de la connexion à la base de données '" + DB_URL_FULL + "'", e);
        }
    }
}