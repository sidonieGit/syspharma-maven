# 💊 SystPharma – Plateforme de gestion de pharmacie

**SystPharma** est une application Java orientée objet conçue pour faciliter la gestion d’une pharmacie moderne.
Elle offre une interface pour les **Clients**, les **Agents de pharmacie** et les **Administrateurs**, leur permettant d'interagir avec les fonctionnalités essentielles de gestion des médicaments, des commandes, des paniers, des assurances et des statistiques.

---

## 🚀 Fonctionnalités Clés (Version 3 - Console)

### 👥 Gestion des Acteurs
- **Client** : Recherche, commande et paiement des médicaments.
- **Agent de pharmacie** : Gère les médicaments, les assurances et les commandes.
- **Administrateur** : Supervise l'ensemble des utilisateurs, pharmacies et accède aux statistiques globales.

### 🔐 Authentification & Compte
- Connexion / Déconnexion sécurisée.
- Création de compte client.
- Mise à jour des informations de compte.
- Système de rôles intégré : `ADMIN`, `CLIENT`, `AGENT_PHARMACIE`.
- Gestion du statut des comptes : `ACTIF`, `DESACTIVE`.

### 🧪 Gestion des Médicaments & Assurances
- **Recherche de médicaments avancée** : Par nom, par catégorie, ou dans une pharmacie spécifique.
- **Opérations CRUD sur les médicaments** : Ajout, modification, suppression (pour les agents).
- Recherche d'assurance par nom.
- **Opérations CRUD sur les assurances** : Ajout, modification, suppression.

### 🛒 Panier & Commande
- Gestion flexible du panier : Ajout, suppression d'articles, incrémentation/décrémentation des quantités, vidage du panier.
- Calcul automatique du montant total.
- Passage de commande (associée à une pharmacie).
- Options de paiement diversifiées : `ESPECE`, `MOMO` (Mobile Money), etc.

### 📊 Statistiques & Rapports
- Calcul du chiffre d’affaires sur une période donnée.
- Suivi du nombre de clients.
- Identification des médicaments les plus commandés.
- Vue sur le nombre total de produits vendus.

---

## 🛠️ Architecture & Bonnes Pratiques

Ce projet adhère à des principes de conception robustes et des pratiques de développement modernes :

-   ✅ **Architecture multicouche** : Organisation claire en 4 couches distinctes (`domaine`, `dao`, `service`, `presentation`) pour une meilleure modularité et maintenabilité.
-   ✅ **Sécurité des requêtes** : Utilisation systématique de `PreparedStatement` pour prévenir les injections SQL.
-   ✅ **Gestion centralisée de la connexion JDBC** : Factorisation de la logique de connexion à la base de données via la classe `ConnexionDB`.
-   ✅ **Configuration externalisée** : Paramètres de connexion à la base de données gérés via des fichiers `.properties` (avec filtrage Maven pour les profils d'environnement).
-   ✅ **Journalisation avancée** : Implémentation d'un système de logging centralisé avec `Log4j` pour un suivi détaillé des opérations et des anomalies.
-   ✅ **Gestion personnalisée des exceptions** : Hiérarchie d'exceptions (`DatabaseException`, `BusinessException`) pour une gestion d'erreurs claire et spécifique.
-   ✅ **Programmation fonctionnelle** : Intégration des fonctionnalités de **Stream API** et **Expressions Lambda** (Java 8+) pour un code plus concis et expressif.
-   ✅ **Automatisation du build** : Utilisation de **Maven** pour la gestion des dépendances, la compilation et le packaging du projet.

---

## 📦 Modèle Métier

Les principales entités modélisées dans ce projet sont :

| Entité             | Attributs clés | Relations importantes |
|--------------------|----------------|-----------------------|
| **Client** | `id`, `nom`, `prenom`, `email` (unique), `password`, `role`, `statut` | - Passe des `Commandes` |
| **Agent Pharmacie**| `id`, `nom`, `prenom`, `matricule`, `email`, `password`, `role`, `statut` | - Gère `Médicaments`, `Assurances` |
| **Administrateur** | `id`, `nom`, `prenom`, `email`, `password`, `role`, `statut` | - Supervise tous les acteurs |
| **Pharmacie** | `id`, `désignation`, `email`, `adresse`, `téléphone`, `directeur`, `horaires` | - Possède un `Stock`, gère des `Assurances` |
| **Médicament** | `id`, `désignation`, `prix`, `description`, `catégorie` | - Est contenu dans `Stock` et `Article Panier` |
| **Générique** | `id`, `désignation`, `prix`, `description` | - Version générique d'un `Médicament` original |
| **Catégorie** | `id`, `désignation` | - Regroupe les `Médicaments` |
| **Assurance** | `id`, `numéro unique`, `désignation`, `description` | - Appliquée aux `Commandes` |
| **Panier** | `id`, `état` (`EN_COURS`, `VALIDE`, `VIDE`), `client` | - Contient des `Article Panier` |
| **Article Panier** | `id`, `quantité`, `prix_unitaire` | - Référence un `Médicament` et un `Panier` |
| **Commande** | `id`, `numéro`, `date`, `statut`, `montant` | - Associée à un `Client`, une `Pharmacie`, un `Panier` et un `Paiement` |
| **Paiement** | `id`, `montant`, `mode_de_paiement`, `date` | - Associé à une `Commande` |
| **Statistique** | (Entité conceptuelle) | - Représente des agrégats pour les rapports |

---

## 📊 Diagrammes

### 🧰 Diagramme de Cas d'Utilisation (Use Case Diagram)

![use case diagram](src/com/sido/syspharma/presentation/img/UseCaseDiagV9Sido.PNG)

### 📘 Diagrammes de Classes

**Diagramme de Classes principal :**
![class diagram](src/com/sido/syspharma/presentation/img/ClassDiagramV10.jpg)

**Diagramme de Classes avec découpage moderne (incluant les classes de service) :**
![class diagram](src/com/sido/syspharma/presentation/img/ClassDiagramServiceV10.jpg)

### 📁 Structure du Projet

![Structure du projet](src/com/sido/syspharma/presentation/img/StructureProjet.PNG)

---

## ⚙️ Technologies & Outils

-   **Langage** : Java 17
-   **Base de données** : MySQL
-   **Connectivité BD** : JDBC (Java Database Connectivity)
-   **Framework de Build** : Apache Maven
-   **Conception d'Architecture** : Pattern DAO (Data Access Object) et Service
-   **Configuration** : Fichiers `.properties` pour l'externalisation de la configuration (support I18N/filtrage d'environnement)
-   **Journalisation** : Log4j 1.2.17
-   **Environnement de Développement Intégré (IDE)** : IntelliJ IDEA
-   **Système de Contrôle de Version** : Git / GitHub

---

## 📈 État d’Avancement du Projet

| Tâche                                  | Statut | Description                                             |
| :------------------------------------- | :----- | :------------------------------------------------------ |
| Architecture en couches                | ✅      | Implémentation complète des couches `domaine`, `dao`, `service`, `presentation`. |
| Connexion JDBC factorisée              | ✅      | Connexion à la base de données gérée de manière centralisée. |
| Services spécialisés                   | ✅      | Logique métier encapsulée dans des services dédiés.     |
| DAO avec interfaces et implémentations | ✅      | Accès aux données structuré via des interfaces DAO.     |
| Gestion d’erreurs personnalisée        | ✅      | Exceptions spécifiques pour les erreurs de base de données et métiers. |
| Journalisation Log4j                   | ✅      | Intégration de Log4j pour le traçage et le débogage.    |
| Lambda / Stream Java 8+                | ✅      | Utilisation des fonctionnalités modernes de Java 8+.    |
| Interface Console                      | ✅      | Interface utilisateur en ligne de commande fonctionnelle. |
| Maven / Automatisation                 | ✅      | Gestion complète du build, des dépendances et du filtrage. |
| Interface Swing / Web                  | ⏳      | Développement futur d'une interface graphique ou web.   |

---

## 📂 Fichiers Clés du Projet

-   `/tp-dao/src/main/resources/db.properties` : Fichier de configuration de la base de données (avec placeholders).
-   `/tp-dao/src/main/resources/filters/dev.properties` : Fichier de filtre pour l'environnement de développement (contient les identifiants sensibles).
-   `/logs/syspharma.log` : Fichier de journalisation où sont stockés tous les logs de l'application (info, erreur, etc.).
-   `/lib/` : Répertoire contenant les drivers JDBC MySQL et la bibliothèque Log4j (si non géré par Maven).
-   `/tp-presentation/src/main/java/com/sido/syspharma/presentation/Main.java` : Le point d'entrée principal de l'application en mode console.

---

## 🔐 Sécurité & Bonnes Pratiques

-   **Protection contre les injections SQL** : Toutes les requêtes SQL sont préparées (`PreparedStatement`).
-   **Externalisation des configurations** : Les informations sensibles (identifiants de base de données) sont externalisées dans des fichiers de propriétés, gérés et ignorés par Git.
-   **Traçabilité des anomalies** : Un système de logging robuste permet de tracer les événements et de détecter les anomalies.
-   **Absence d'informations sensibles en dur** : Aucune information confidentielle n'est codée en dur dans le code source de l'application.

---

## 🤝 Contributions

Les contributions sont les bienvenues ! Si vous avez des suggestions d'amélioration ou des corrections, n'hésitez pas à ouvrir une `Issue` ou à soumettre une `Pull Request`.

---

## 📄 Licence

Ce projet est sous licence [MIT License](https://opensource.org/licenses/MIT) (exemple, à adapter).

---

## 👩‍💻 Auteur

Ce projet a été réalisé dans le cadre du TP Java Orienté Objet + JDBC (Objis).
Guidé par les bonnes pratiques professionnelles et pédagogiques.

**Période de réalisation :** Février 2024 – Mai 2025

-   **LinkedIn** : [www.linkedin.com/in/sidonie-djuissi-fohouo](https://www.linkedin.com/in/sidonie-djuissi-fohouo)
-   **Email** : sidoniedjuissifohouo@gmail.com
-   **Téléphone** : +237 696 00 23 77

Dernière mise à jour : 30/05/2025