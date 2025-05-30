package com.sido.syspharma.presentation;

import com.sido.syspharma.dao.database.TableCreator;
import com.sido.syspharma.dao.impl.ClientDAOImpl;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.enums.Role; // Ajoutez cet import si ce n'est pas déjà fait
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.service.ServiceClient;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {


        // TENTER DE CRÉER LES TABLES AU DÉMARRAGE
        try {
            TableCreator.createTableClientIfNotExists();
            TableCreator.createTableMedicamentIfNotExists(); // Et les autres tables si besoin
            logger.info("Vérification/Création des tables terminée.");
        } catch (com.sido.syspharma.exceptions.DatabaseException e) {
            logger.fatal("Échec de l'initialisation des tables de la base de données : " + e.getMessage(), e);
            System.err.println("ERREUR CRITIQUE : Impossible d'initialiser la base de données. L'application va s'arrêter.");
            return; // Arrêter l'application si les tables ne peuvent pas être créées
        }

        Scanner scanner = new Scanner(System.in);

        ServiceClient serviceClient = new ServiceClient(new ClientDAOImpl());

        System.out.println("=== 📦 SystPharma - Connexion Client ===");
        System.out.print("1️⃣ Créer un compte ou 2️⃣ Se connecter ? [1/2] : ");
        String choix = scanner.nextLine();

        try {
            if ("1".equals(choix)) { // Utilisation de .equals pour éviter NullPointerException
                System.out.println("📝 Création de compte");
                System.out.print("Nom : "); String nom = scanner.nextLine();
                System.out.print("Prénom : "); String prenom = scanner.nextLine();
                System.out.print("Email : "); String email = scanner.nextLine();
                System.out.print("Adresse : "); String adresse = scanner.nextLine();
                System.out.print("Téléphone : "); String tel = scanner.nextLine();
                System.out.print("Mot de passe : "); String pwd = scanner.nextLine();

                // 🚨 CORRECTION ICI : Assigner un rôle non nul, par exemple Role.CLIENT
                Client client = new Client(nom, prenom, email, adresse, tel, pwd, Role.CLIENT); // Assurez-vous que Role.CLIENT existe et est approprié

                if (serviceClient.creerCompte(client)) {
                    System.out.println("✅ Compte client créé !");
                    logger.info("✅ Compte client créé : " + email); // Ajout de log
                }

            } else if ("2".equals(choix)) { // Utilisation de .equals pour éviter NullPointerException
                System.out.print("Email : "); String email = scanner.nextLine();
                System.out.print("Mot de passe : "); String pwd = scanner.nextLine();

                if (serviceClient.seConnecter(email, pwd)) {
                    System.out.println("🔓 Connexion réussie !");
                    logger.info("🔓 Connexion réussie : " + email); // Ajout de log
                } else {
                    System.out.println("❌ Échec de connexion.");
                    logger.warn("❌ Échec de connexion pour : " + email); // Ajout de log
                }
            } else {
                System.out.println("⛔ Choix invalide.");
                logger.warn("Choix invalide fourni : " + choix); // Ajout de log
            }

        } catch (BusinessException e) {
            logger.error("💥 Erreur métier : " + e.getMessage(), e); // Log complet de l'exception
            System.out.println("❌ Erreur : " + e.getMessage());
        } catch (Exception e) { // Capture toutes les autres exceptions inattendues
            logger.fatal("🐛 Une erreur inattendue est survenue : " + e.getMessage(), e);
            System.out.println("❌ Une erreur inattendue est survenue : " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            logger.info("Application terminée."); // Log de fin
        }
    }
}