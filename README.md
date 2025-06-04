# 💊 SystPharma – Plateforme de gestion de pharmacie

**SystPharma** est une application Java orientée objet conçue pour faciliter la gestion d’une pharmacie moderne.
Elle offre une interface pour les **Clients**, les **Agents de pharmacie** et les **Administrateurs**, leur permettant d'interagir avec les fonctionnalités essentielles de gestion des médicaments, des commandes, des paniers, des assurances et des statistiques.

---

## 🚀 Fonctionnalités Clés (Version Actuelle - Console)

### 👥 Gestion des Acteurs
- **Client** : S'enregistre, se connecte, recherche des médicaments, gère son panier, passe des commandes et effectue des paiements.
- **Agent de pharmacie** : Gère les médicaments (CRUD), les catégories, les assurances (CRUD) et les commandes des clients.
- **Administrateur** : Supervise l'ensemble des utilisateurs, des pharmacies (si applicable), et accède aux statistiques globales.

### 🔐 Authentification & Compte
- Connexion / Déconnexion sécurisée.
- Création de compte client.
- Mise à jour des informations de compte (fonctionnalité future ou partielle).
- Système de rôles intégré : `Role.ADMINISTRATEUR`, `Role.CLIENT`, `Role.AGENT` (ou noms exacts de votre enum `Role`).
- Gestion du statut des comptes : `StatutCompte.ACTIF`, `StatutCompte.INACTIF`, `StatutCompte.DESACTIVE` (ou noms exacts de votre enum `StatutCompte`).

### 🧪 Gestion des Médicaments, Catégories & Assurances
- **Recherche de médicaments avancée** : Par nom. (Futures améliorations : par catégorie, dans une pharmacie spécifique).
- **Opérations CRUD sur les médicaments** : Ajout, modification, suppression (typiquement pour les agents/admins).
- Gestion des catégories de médicaments (CRUD si entité distincte).
- Recherche d'assurance par nom.
- **Opérations CRUD sur les assurances** (si implémenté).

### 🛒 Panier & Commande
- Gestion flexible du panier : Ajout, suppression d'articles, modification des quantités, vidage du panier.
- Calcul automatique du montant total.
- Passage de commande (potentiellement associée à une pharmacie si cette entité existe).
- Options de paiement diversifiées (ex: `ESPECE`, `MOMO`).

### 📊 Statistiques & Rapports (Fonctionnalités futures ou conceptuelles pour la version console)
- Calcul du chiffre d’affaires sur une période donnée.
- Suivi du nombre de clients.
- Identification des médicaments les plus commandés.
- Vue sur le nombre total de produits vendus.

---

## 🛠️ Architecture & Bonnes Pratiques

Ce projet adhère à des principes de conception robustes et des pratiques de développement modernes :

-   ✅ **Architecture multicouche** : Organisation claire en couches distinctes (`domaine`, `dao`, `service`, `presentation`) pour une meilleure modularité et maintenabilité.
-   ✅ **Sécurité des requêtes SQL** : Utilisation systématique de `PreparedStatement` pour prévenir les injections SQL.
-   ✅ **Gestion de la connexion JDBC** : Obtention centralisée de la connexion via `ConnexionDB` et gestion des ressources JDBC avec `try-with-resources` dans les DAOs. `AbstractDAO` fournit des méthodes utilitaires pour exécuter les requêtes.
-   ✅ **Configuration externalisée** : Paramètres de connexion à la base de données gérés via des fichiers `.properties` (avec filtrage Maven pour les profils d'environnement).
-   ✅ **Journalisation (Logging)** : Implémentation d'un système de logging avec `Log4j` pour un suivi détaillé des opérations et des anomalies.
-   ✅ **Gestion personnalisée des exceptions** : Hiérarchie d'exceptions (`DatabaseException`, `BusinessException`) pour une gestion d'erreurs claire et spécifique.
-   ✅ **Programmation fonctionnelle Java 8+** : Utilisation de Stream API et d'Expressions Lambda (par exemple, pour les `RowMapper` dans les DAOs).
-   ✅ **Tests Unitaires** : Implémentation de tests unitaires avec JUnit 5 et Mockito pour la couche service, favorisant la robustesse et la maintenabilité du code.
-   ✅ **Automatisation du build** : Utilisation de **Apache Maven** pour la gestion des dépendances, la compilation, les tests et le packaging du projet.

---

## 📦 Modèle Métier (Entités Principales)

| Entité             | Attributs clés (non exhaustif) | Relations importantes (exemples) |
|--------------------|--------------------------------|-----------------------------------|
| **Client** (hérite de `Utilisateur`) | `id`, `email` (unique), `password`, `compte` (contenant `role`, `statut`) | - Passe des `Commandes`, possède un `Panier` |
| **Agent Pharmacie** (hérite de `Utilisateur`) | `id`, `matricule`, `email`, `password`, `compte` | - Gère `Médicaments`, `Assurances` |
| **Administrateur** (hérite de `Utilisateur`) | `id`, `email`, `password`, `compte` | - Supervise le système |
| **Pharmacie** (si modélisée) | `id`, `nom`, `adresse` | - Possède un `Stock`, gère des `Assurances` |
| **Médicament** | `id` (PK auto-générée), `designation`, `prix`, `description`, `image` | - Appartient à une `Categorie`, est dans `Stock` et `ArticlePanier` |
| **Categorie** | `id` (PK), `designation` (unique) | - Regroupe des `Médicaments` |
| **Assurance** (si modélisée) | `id` (PK), `nom`, `taux_couverture` | - Peut être appliquée aux `Commandes` |
| **Panier** | `id` (PK), `etat` | - Associé à un `Client`, contient des `ArticlePanier` |
| **Article Panier** | `id` (PK), `quantite` | - Référence un `Medicament`, appartient à un `Panier` |
| **Commande** | `id` (PK), `numero`, `date`, `statut`, `montant_total` | - Associée à `Client`, `Pharmacie` (optionnel), `Panier` (source), `Paiement` |
| **Paiement** | `id` (PK), `montant`, `mode_paiement`, `date_paiement` | - Associé à une `Commande` |
| **Compte** (dans `Utilisateur`) | `email` (identifiant), `password`, `role`, `statut` | - Associé à un `Utilisateur` |

---

## 📊 Diagrammes

### 🧰 Diagramme de Cas d'Utilisation (Use Case Diagram)
![Diagramme de Cas d'Utilisation](tp-presentation/src/main/resources/img/UseCaseDiagV9Sido.PNG)

### 📘 Diagrammes de Classes
**Diagramme de Classes principal :**
![Diagramme de Classes Principal](tp-presentation/src/main/resources/img/ClassDiagramV10.jpg)

**Diagramme de Classes avec services :**
![Diagramme de Classes avec Services](tp-presentation/src/main/resources/img/ClassDiagramServiceV10.jpg)




---

## ⚙️ Technologies & Outils

-   **Langage** : Java 17 (ou la version que vous utilisez)
-   **Base de données** : MySQL (version à spécifier si pertinent)
-   **Connectivité BD** : JDBC (Java Database Connectivity)
-   **Framework de Build** : Apache Maven
-   **Tests Unitaires** : JUnit 5, Mockito
-   **Conception d'Architecture** : Multicouche, Pattern DAO (Data Access Object) et Service.
-   **Configuration** : Fichiers `.properties` avec filtrage Maven pour profils d'environnement.
-   **Journalisation** : Log4j 1.2.17
-   **IDE** : IntelliJ IDEA (ou autre)
-   **Contrôle de Version** : Git / GitHub

---

## 📈 État d’Avancement du Projet

| Tâche                                      | Statut | Description                                                                  |
| :----------------------------------------- | :----- | :--------------------------------------------------------------------------- |
| Architecture en couches (Domaine, DAO, Service, Présentation) | ✅      | Implémentation complète.                                                    |
| Connexion JDBC & Gestion des ressources    | ✅      | Centralisée via `ConnexionDB` et `AbstractDAO` avec `try-with-resources`.    |
| Services métier dédiés                     | ✅      | Logique métier encapsulée.                                                  |
| DAO avec interfaces et implémentations     | ✅      | Accès aux données structuré.                                                 |
| Gestion d’erreurs personnalisée            | ✅      | Exceptions `DatabaseException` et `BusinessException`.                      |
| Journalisation Log4j                       | ✅      | Intégration pour traçage et débogage.                                       |
| Utilisation de Java 8+ (Lambda/Stream)     | ✅      | Code modernisé (ex: `RowMapper`).                                            |
| Tests Unitaires (Couche Service)           | ✅      | Implémentation avec JUnit 5 & Mockito.                                       |
| Interface Console (CLI)                    | ✅      | Interface utilisateur fonctionnelle pour les opérations de base.              |
| Maven (Build, Dépendances, Profils)        | ✅      | Gestion complète du projet.                                                 |
| Interface Graphique (Swing/JavaFX) ou Web  | ⏳      | Développement futur envisagé.                                                |
| Intégration Spring Framework               | ⏳      | Prochaine étape envisagée pour la gestion des beans et DI.                   |

---

## 📂 Fichiers Clés du Projet

-   `pom.xml` (à la racine) : Fichier principal de configuration Maven (POM parent).
-   `tp-domaine/`, `tp-dao/`, `tp-service/`, `tp-presentation/` : Modules Maven du projet.
-   `tp-dao/src/main/resources/db.properties` : Fichier modèle pour la configuration de la base de données (avec placeholders).
-   `tp-dao/src/main/resources/filters/dev.properties` : Fichier de filtre pour l'environnement de développement (contient les identifiants BD sensibles - **doit être dans `.gitignore`**).
-   `tp-presentation/src/main/java/com/sido/syspharma/presentation/Main.java` : Point d'entrée de l'application console.
-   `logs/syspharma.log` (généré à l'exécution, chemin configurable via `log4j.properties`) : Fichier de journalisation.

---

## 🔐 Sécurité & Bonnes Pratiques

-   **Protection contre les injections SQL** : Utilisation systématique de `PreparedStatement`.
-   **Externalisation des configurations sensibles** : Identifiants de base de données dans des fichiers de propriétés filtrés et ignorés par Git.
-   **Traçabilité des anomalies** : Logging robuste avec Log4j.
-   **Code Source Propre** : Absence d'informations confidentielles codées en dur.

---

### Étapes d'exécution

1.  **Prérequis :**
    *   Java JDK 17 (ou la version spécifiée dans `pom.xml`) installé et configuré.
    *   Apache Maven installé et configuré.
    *   Serveur MySQL accessible et en cours d'exécution.
    *   Créez manuellement la base de données : `CREATE DATABASE syspharma_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;` (ou le nom que vous utilisez).

2.  **Clonez le dépôt du projet :**
    ```bash
    git clone [URL_DU_VOTRE_DEPOT_GITHUB]
    cd SysPharmaMaven
    ```
    *(Remplacez `[URL_DU_VOTRE_DEPOT_GITHUB]` par l'URL réelle de votre dépôt)*

3.  **Configurez les identifiants de la base de données :**
    *   Créez ou ouvrez le fichier `tp-dao/src/main/resources/filters/dev.properties`.
    *   Assurez-vous qu'il contienne :
        ```properties
        db.url=jdbc:mysql://localhost:3306/syspharma_dev # Adaptez si nécessaire
        db.username=votre_utilisateur_mysql
        db.password=votre_mot_de_passe_mysql
        ```
    *   **Attention** : Ce fichier `dev.properties` doit être listé dans votre `.gitignore` pour ne pas commiter vos identifiants.

4.  **Construisez le projet avec Maven :**
    À la racine du projet (`SysPharmaMaven`), exécutez :
    ```bash
    mvn clean install -Pdev
    ```
    *(L'option `-Pdev` active le profil de développement, qui utilise `dev.properties` pour le filtrage des ressources.)*

5.  **Exécutez l'application console :**
    Depuis la racine du projet (`SysPharmaMaven`) :
    ```bash
    mvn exec:java -pl tp-presentation
    ```
    *(Cette commande exécute la classe `com.sido.syspharma.presentation.Main`.)*

    *(Note : La création d'un JAR exécutable "fat" avec toutes les dépendances n'est pas configurée par défaut dans ce projet. L'exécution se fait via Maven pour simplifier.)*

---

## 🤝 Contributions

Les contributions sont les bienvenues ! Veuillez ouvrir une `Issue` pour discuter des changements majeurs ou soumettre une `Pull Request` pour des corrections ou améliorations mineures.

---


## 👩‍💻 Auteur

Projet réalisé dans le cadre d'un TP Java chez Obis partenaire de 10000codeurs 

**Période :** Février 2024 – Juin 2025 
Dernière mise à jour : 05 Juin 2025 

-   **LinkedIn** : [www.linkedin.com/in/sidonie-djuissi-fohouo](https://www.linkedin.com/in/sidonie-djuissi-fohouo)
-   **Email** : sidoniedjuissifohouo@gmail.com
-   **Téléphone** : +237 696 00 23 77

