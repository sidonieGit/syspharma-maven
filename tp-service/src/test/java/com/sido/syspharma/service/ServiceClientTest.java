// Déclare le package où se trouve cette classe de test.
package com.sido.syspharma.service;

// Imports nécessaires.
import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.domaine.model.Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Active l'intégration Mockito-JUnit 5.
@DisplayName("Tests Unitaires pour la classe ServiceClient") // Nom global pour cette suite de tests.
class ServiceClientTest {

    @Mock // Crée un mock de IClientDAO. Mockito contrôlera son comportement.
    private IClientDAO clientDAO;

    @InjectMocks // Crée une instance de ServiceClient et y injecte le mock clientDAO.
    private ServiceClient serviceClient;

    // Variables pour les objets Client de test, initialisées avant chaque test.
    private Client clientValidePourTest;
    private Client clientAvecEmailExistant;
    private Client clientPourListe1;
    private Client clientPourListe2;

    @BeforeEach // Méthode exécutée avant chaque @Test.
    void setUp() {
        // Initialisation des objets Client avec des données de test.
        clientValidePourTest = new Client("ValideNom", "ValidePrenom", "valide@example.com", "1 Valide St", "111-1111", "password123", Role.CLIENT);
        clientValidePourTest.setId(1L); // Simule un ID post-persistance.

        clientAvecEmailExistant = new Client("ExistantNom", "ExistantPrenom", "existant@example.com", "2 Existant Ave", "222-2222", "passwordExistant", Role.CLIENT);
        clientAvecEmailExistant.setId(2L);

        clientPourListe1 = new Client("ListeNom1", "ListePrenom1", "liste1@example.com", "Addr1", "333-1111", "passListe1", Role.CLIENT);
        clientPourListe1.setId(3L);
        clientPourListe2 = new Client("ListeNom2", "ListePrenom2", "liste2@example.com", "Addr2", "333-2222", "passListe2", Role.CLIENT);
        clientPourListe2.setId(4L);
    }

    // @Nested regroupe les tests pour la méthode creerCompte.
    @Nested
    @DisplayName("Tests pour la méthode creerCompte(Client client)")
    class CreerCompteTests {

        @Test // Marque comme un cas de test.
        @DisplayName("Devrait créer un compte avec succès si données valides et email non existant")
        void creerCompte_Success_When_ValidDataAndEmailNotExistsAndDAOInserts() throws BusinessException, DatabaseException {
            // Arrange : Configurer le comportement des mocks.
            // Simule que l'email n'est pas trouvé dans la DB.
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.empty());
            // Simule que l'insertion dans le DAO réussit.
            when(clientDAO.inserer(clientValidePourTest)).thenReturn(true);

            // Act : Exécuter la méthode testée.
            boolean resultat = serviceClient.creerCompte(clientValidePourTest);

            // Assert : Vérifier le résultat et les interactions.
            assertTrue(resultat, "La création de compte devrait retourner true.");
            // Vérifier que trouverParEmail a été appelé une fois.
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
            // Vérifier que inserer a été appelé une fois.
            verify(clientDAO, times(1)).inserer(clientValidePourTest);
        }

        @Test
        @DisplayName("Devrait retourner false si client valide, email non existant mais insertion DAO retourne false")
        void creerCompte_ReturnsFalse_When_DAOInsertReturnsFalse() throws BusinessException, DatabaseException {
            // Arrange
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.empty());
            when(clientDAO.inserer(clientValidePourTest)).thenReturn(false); // Simule un échec silencieux du DAO.

            // Act
            boolean resultat = serviceClient.creerCompte(clientValidePourTest);

            // Assert
            assertFalse(resultat, "La création de compte devrait retourner false.");
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
            verify(clientDAO, times(1)).inserer(clientValidePourTest);
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si l'email du client existe déjà")
        void creerCompte_ThrowsBusinessException_When_EmailAlreadyExists() throws DatabaseException {
            // Arrange : Simule que l'email existe déjà.
            when(clientDAO.trouverParEmail(clientAvecEmailExistant.getEmail())).thenReturn(Optional.of(clientAvecEmailExistant));

            // Act & Assert : Vérifie qu'une BusinessException est lancée.
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientAvecEmailExistant);
            });
            // Vérifie que le message de l'exception est correct.
            assertTrue(thrown.getMessage().contains("Un compte avec l'email '" + clientAvecEmailExistant.getEmail() + "' existe déjà."),
                    "Le message devrait indiquer que l'email existe déjà.");
            verify(clientDAO, times(1)).trouverParEmail(clientAvecEmailExistant.getEmail());
            verify(clientDAO, never()).inserer(any(Client.class)); // L'insertion ne doit pas être appelée.
        }


        @Test
        @DisplayName("Devrait lancer BusinessException si DatabaseException lors de la recherche d'email")
        void creerCompte_ThrowsBusinessException_When_DatabaseExceptionOnEmailCheck() throws DatabaseException {
            // Arrange : Simule une erreur DB lors de la recherche d'email.
            DatabaseException dbEx = new DatabaseException("Erreur DB simulée (recherche email)", new SQLException("Cause SQL"));
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenThrow(dbEx);

            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientValidePourTest);
            });
            // Vérifie que le message est celui de l'exception wrappée par le service.
            assertTrue(thrown.getMessage().contains("Une erreur technique est survenue lors de la création de votre compte"),
                    "Le message devrait être celui de l'exception wrappée.");
            // Vérifie que la cause de la BusinessException est bien la DatabaseException simulée.
            assertSame(dbEx, thrown.getCause(), "La DatabaseException devrait être la cause.");
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
            verify(clientDAO, never()).inserer(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si DatabaseException lors de l'insertion")
        void creerCompte_ThrowsBusinessException_When_DatabaseExceptionOnInsert() throws DatabaseException {
            // Arrange
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.empty()); // Email non trouvé
            DatabaseException dbEx = new DatabaseException("Erreur DAO insertion", new SQLException("Cause SQL"));
            when(clientDAO.inserer(clientValidePourTest)).thenThrow(dbEx); // Lancer l'exception sur inserer

            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientValidePourTest);
            });
            assertTrue(thrown.getMessage().contains("Une erreur technique est survenue lors de la création de votre compte"));
            assertSame(dbEx, thrown.getCause());
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
            verify(clientDAO, times(1)).inserer(clientValidePourTest);
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si le client est null")
        void creerCompte_ClientIsNull_ShouldThrowBusinessException() throws DatabaseException {
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(null);
            });
            // CORRECTION : Le message est maintenant spécifique pour ce cas dans ServiceClient.
            assertTrue(thrown.getMessage().contains("informations du client ne peuvent pas être nulles"),
                    "Le message d'exception pour client null est incorrect.");
            verify(clientDAO, never()).trouverParEmail(anyString());
            verify(clientDAO, never()).inserer(any(Client.class));
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si nom du client est vide")
        void creerCompte_ClientNomEstVide_ShouldThrowBusinessException() throws DatabaseException {
            // Arrange
            Client clientSansNom = new Client("", "John", "nomvide@example.com", "Addr", "123", "password123", Role.CLIENT);
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientSansNom);
            });
            // CORRECTION : Le message est maintenant spécifique.
            assertTrue(thrown.getMessage().contains("nom du client ne peut pas être vide"),
                    "Le message d'exception pour nom vide est incorrect.");
            verifyNoInteractions(clientDAO); // Aucune interaction avec le DAO si la validation échoue avant.
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si l'email du client est invalide")
        void creerCompte_ClientEmailInvalide_ShouldThrowBusinessException() throws DatabaseException {
            // Arrange
            Client clientEmailInvalide = new Client("Doe", "John", "emailinvalide", "Addr", "123", "password123", Role.CLIENT);
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientEmailInvalide);
            });
            // CORRECTION : Le message est maintenant spécifique.
            assertTrue(thrown.getMessage().contains("Format d'email invalide"),
                    "Le message d'exception pour email invalide est incorrect.");
            verifyNoInteractions(clientDAO);
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si le mot de passe est trop court")
        void creerCompte_ClientPasswordTropCourt_ShouldThrowBusinessException() throws DatabaseException {
            // Arrange
            Client clientMdpCourt = new Client("Doe", "John", "mdpcourt@example.com", "Addr", "123", "pass", Role.CLIENT);
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.creerCompte(clientMdpCourt); // Cet appel devrait maintenant lever l'exception
            });
            // CORRECTION : Le message est maintenant spécifique.
            assertTrue(thrown.getMessage().contains("mot de passe doit contenir au moins 6 caractères"),
                    "Le message d'exception pour mot de passe trop court est incorrect.");
            verifyNoInteractions(clientDAO);
        }
    }


    // @Nested regroupe les tests pour la méthode seConnecter.
    @Nested
    @DisplayName("Tests pour la méthode seConnecter(String email, String motDePasse)")
    class SeConnecterTests {

        @Test
        @DisplayName("Devrait retourner true avec des identifiants corrects")
        void seConnecter_Success() throws BusinessException, DatabaseException {
            // Arrange
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));
            // Act
            boolean resultat = serviceClient.seConnecter(clientValidePourTest.getEmail(), clientValidePourTest.getPassword());
            // Assert
            assertTrue(resultat, "La connexion devrait réussir.");
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
        }

        @Test
        @DisplayName("Devrait retourner false avec un mot de passe incorrect")
        void seConnecter_WrongPassword() throws BusinessException, DatabaseException {
            // Arrange
            when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));
            // Act
            boolean resultat = serviceClient.seConnecter(clientValidePourTest.getEmail(), "motdepasseIncorrect");
            // Assert
            assertFalse(resultat, "La connexion devrait échouer.");
            verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
        }

        @Test
        @DisplayName("Devrait retourner false si l'email n'est pas trouvé")
        void seConnecter_EmailNotFound() throws BusinessException, DatabaseException {
            // Arrange
            when(clientDAO.trouverParEmail("unknown@example.com")).thenReturn(Optional.empty());
            // Act
            boolean resultat = serviceClient.seConnecter("unknown@example.com", "anypassword");
            // Assert
            assertFalse(resultat, "La connexion devrait échouer.");
            verify(clientDAO, times(1)).trouverParEmail("unknown@example.com");
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si DatabaseException lors de la recherche")
        void seConnecter_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
            // Arrange
            DatabaseException dbEx = new DatabaseException("Erreur DB recherche", new SQLException("Cause SQL"));
            when(clientDAO.trouverParEmail(anyString())).thenThrow(dbEx);
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.seConnecter("any@email.com", "anypassword");
            });
            // Le message exact vient de votre ServiceClient.seConnecter() : "Connexion impossible"
            assertTrue(thrown.getMessage().contains("Connexion impossible"), "Le message de BusinessException est incorrect.");
            assertSame(dbEx, thrown.getCause());
            verify(clientDAO, times(1)).trouverParEmail(anyString());
        }
    }


    // @Nested regroupe les tests pour la méthode getTousLesClients.
    @Nested
    @DisplayName("Tests pour la méthode getTousLesClients()")
    class GetTousLesClientsTests {

        @Test
        @DisplayName("Devrait retourner une liste de clients si le DAO en fournit")
        void getTousLesClients_Success() throws BusinessException, DatabaseException {
            // Arrange
            List<Client> listeAttendue = Arrays.asList(clientPourListe1, clientPourListe2);
            when(clientDAO.listerTous()).thenReturn(listeAttendue);
            // Act
            List<Client> resultat = serviceClient.getTousLesClients();
            // Assert
            assertNotNull(resultat);
            assertEquals(2, resultat.size());
            assertSame(listeAttendue, resultat, "La liste retournée devrait être celle du DAO.");
            verify(clientDAO, times(1)).listerTous();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide si le DAO retourne une liste vide")
        void getTousLesClients_EmptyList() throws BusinessException, DatabaseException {
            // Arrange
            when(clientDAO.listerTous()).thenReturn(Collections.emptyList());
            // Act
            List<Client> resultat = serviceClient.getTousLesClients();
            // Assert
            assertNotNull(resultat);
            assertTrue(resultat.isEmpty());
            verify(clientDAO, times(1)).listerTous();
        }

        @Test
        @DisplayName("Devrait lancer BusinessException si DatabaseException survient lors du listage")
        void getTousLesClients_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
            // Arrange
            DatabaseException dbEx = new DatabaseException("Erreur DB listage", new SQLException("Cause SQL"));
            when(clientDAO.listerTous()).thenThrow(dbEx);
            // Act & Assert
            BusinessException thrown = assertThrows(BusinessException.class, () -> {
                serviceClient.getTousLesClients();
            });
            // Le message exact vient de votre ServiceClient.getTousLesClients() : "Erreur de récupération des clients"
            assertTrue(thrown.getMessage().contains("Erreur de récupération des clients"));
            assertSame(dbEx, thrown.getCause());
            verify(clientDAO, times(1)).listerTous();
        }
    }
}