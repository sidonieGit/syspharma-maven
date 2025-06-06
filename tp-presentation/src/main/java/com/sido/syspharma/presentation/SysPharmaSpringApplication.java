package com.sido.syspharma.presentation;

import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.service.ServiceClient;
// import com.sido.syspharma.service.ServiceMedicamentDB; // Si vous l'utilisez

import org.slf4j.Logger; // SLF4J Logger
import org.slf4j.LoggerFactory; // SLF4J LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sido.syspharma.service", "com.sido.syspharma.dao", "com.sido.syspharma.presentation"})
public class SysPharmaSpringApplication implements CommandLineRunner {

    // CORRECTION : Le logger doit être associé à la classe actuelle (SysPharmaSpringApplication)
    private static final Logger logger = LoggerFactory.getLogger(SysPharmaSpringApplication.class);

    private final ServiceClient serviceClient;
    // private final ServiceMedicamentDB serviceMedicamentDB;

    @Autowired
    public SysPharmaSpringApplication(ServiceClient serviceClient /*, ServiceMedicamentDB serviceMedicamentDB */) {
        this.serviceClient = serviceClient;
        // this.serviceMedicamentDB = serviceMedicamentDB;
    }

    public static void main(String[] args) {
        logger.info("Démarrage de l'application SysPharma Spring Boot...");
        ConfigurableApplicationContext context = SpringApplication.run(SysPharmaSpringApplication.class, args);
        logger.info("Application SysPharma Spring Boot démarrée et contexte initialisé.");
        // Ne fermez pas le contexte ici pour une application interactive en console
        // qui attend des entrées utilisateur dans la méthode run().
        // Le programme se terminera naturellement à la fin de la méthode run()
        // et Spring s'occupera du shutdown.
    }

    @Override
    public void run(String... args) throws Exception {
        // Utilisation du logger pour tracer les événements importants
        logger.info("--- Application SysPharma (Spring Boot) Démarrée ---");
        logger.debug("ServiceClient injecté : {}", (serviceClient != null)); // Utilisation du placeholder SLF4J

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== 📦 SystPharma - Bienvenue ===");
            System.out.print("1️⃣ Créer un compte ou 2️⃣ Se connecter ? [1/2] : ");
            String choixInitial = scanner.nextLine();
            logger.info("Choix initial de l'utilisateur : {}", choixInitial);


// ...
            if ("1".equals(choixInitial)) {
                System.out.println("📝 Création de compte");
                System.out.print("Nom : "); String nom = scanner.nextLine();
                System.out.print("Prénom : "); String prenom = scanner.nextLine(); // Prénom pourrait être optionnel, à vous de voir
                System.out.print("Email : "); String email = scanner.nextLine();
                System.out.print("Adresse : "); String adresse = scanner.nextLine(); // Adresse pourrait être optionnelle
                System.out.print("Téléphone : "); String tel = scanner.nextLine(); // Téléphone pourrait être optionnel
                System.out.print("Mot de passe : "); String pwd = scanner.nextLine();

                // --- VALIDATIONS DE BASE CÔTÉ PRÉSENTATION (pour feedback rapide) ---
                boolean entreesValides = true;
                if (nom == null || nom.trim().isEmpty()) {
                    System.err.println("❌ Le nom ne peut pas être vide.");
                    logger.warn("Tentative de création de compte avec nom vide.");
                    entreesValides = false;
                }
                // Regex simple pour la présentation, le service peut avoir une validation plus stricte si besoin
                if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    System.err.println("❌ Format d'email invalide.");
                    logger.warn("Tentative de création de compte avec email invalide: {}", email);
                    entreesValides = false;
                }
                if (pwd == null || pwd.length() < 6) { // Longueur minimale
                    System.err.println("❌ Le mot de passe doit contenir au moins 6 caractères.");
                    logger.warn("Tentative de création de compte avec mot de passe trop court pour l'email: {}", email);
                    entreesValides = false;
                }

                if (entreesValides) {
                    // Si les validations de base de la présentation passent, on appelle le service.
                    // Le service refera ses propres validations (qui peuvent être plus complètes).
                    Client nouveauClient = new Client(nom, prenom, email, adresse, tel, pwd, Role.CLIENT);
                    logger.info("Tentative de création de compte via le service pour l'email : {}", email);
                    try {
                        if (this.serviceClient.creerCompte(nouveauClient)) {
                            System.out.println("✅ Compte client créé avec succès ! Vous pouvez maintenant vous connecter.");
                            logger.info("Compte client créé avec succès pour : {}", email);
                        } else {
                            // Ce cas est moins probable si creerCompte lève des BusinessException pour les échecs métier
                            System.out.println("❌ Échec de la création du compte (raison métier non spécifiée par exception).");
                            logger.warn("Échec de la création du compte (service a retourné false) pour : {}", email);
                        }
                    } catch (BusinessException e) {
                        // BusinessException levée par le service (ex: email déjà pris, validation métier interne échouée)
                        System.err.println("❌ Erreur lors de la création du compte : " + e.getMessage());
                        logger.error("BusinessException lors de la création du compte pour {} : {}", email, e.getMessage(), e.getCause() != null ? e.getCause() : e);
                    }
                }
                // Si entreesValides est false, l'utilisateur a déjà eu son message d'erreur de la présentation.


            } else if ("2".equals(choixInitial)) {
                System.out.print("Email : "); String emailLogin = scanner.nextLine();
                System.out.print("Mot de passe : "); String pwdLogin = scanner.nextLine(); // Ne pas logger le mot de passe en clair
                logger.info("Tentative de connexion pour l'email : {}", emailLogin);
                try {
                    if (this.serviceClient.seConnecter(emailLogin, pwdLogin)) {
                        System.out.println("🔓 Connexion réussie via Spring!");
                        logger.info("Connexion réussie pour : {}", emailLogin);
                        // afficherMenuClientConnecte(scanner, this.serviceClient);
                    } else {
                        System.out.println("❌ Échec de connexion. Email ou mot de passe incorrect.");
                        logger.warn("Échec de connexion (identifiants incorrects) pour : {}", emailLogin);
                    }
                } catch (BusinessException e) {
                    System.err.println("❌ Erreur métier lors de la connexion : " + e.getMessage());
                    logger.error("Erreur métier lors de la connexion pour {} : {}", emailLogin, e.getMessage(), e); // Log avec la stack trace
                }
            } else {
                System.out.println("⛔ Choix invalide.");
                logger.warn("Choix utilisateur invalide : {}", choixInitial);
            }
        } // Le scanner est fermé ici
        logger.info("--- Application SysPharma Terminée (ou fin de la session interactive) ---");
    }

    // private void afficherMenuClientConnecte(Scanner scanner, ServiceClient clientService) { ... }
}