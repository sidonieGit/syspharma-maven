package com.sido.syspharma.dao.database; // Assurez-vous que le package est correct

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AppConfig {

    private static final Logger logger =  LoggerFactory.getLogger(AppConfig.class); // Déclaration du logger
    private static final Properties props = new Properties();
    private static final String CONFIG_FILE_NAME = "config.properties"; // Nom du fichier de configuration

    static {
        // Le bloc static s'exécute une seule fois au chargement de la classe
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input == null) {
                // Si le fichier n'est pas trouvé, enregistrez une erreur critique et lancez une exception
                logger.error("Fichier de configuration '" + CONFIG_FILE_NAME + "' introuvable dans le classpath. L'application ne peut pas démarrer sans configuration.");
                throw new RuntimeException("Erreur critique : Fichier de configuration manquant.");
            }
            props.load(input);
            logger.info("Configuration chargée avec succès depuis '" + CONFIG_FILE_NAME + "'.");
        } catch (Exception e) {
            // En cas d'erreur pendant le chargement des propriétés, enregistrez l'erreur et la stack trace
            logger.error("Erreur fatale lors du chargement de la configuration depuis '" + CONFIG_FILE_NAME + "' : " + e.getMessage(), e);
            throw new RuntimeException("Erreur critique lors du chargement de la configuration.", e);
        }
    }

    /**
     * Récupère la valeur d'une propriété à partir de la configuration.
     *
     * @param key La clé de la propriété à récupérer.
     * @return La valeur de la propriété, ou null si la clé n'existe pas.
     */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            // Journalisez un avertissement si une clé est demandée mais non trouvée.
            logger.warn("La clé de configuration '" + key + "' n'a pas été trouvée dans " + CONFIG_FILE_NAME + ".");
        }
        return value;
    }
}