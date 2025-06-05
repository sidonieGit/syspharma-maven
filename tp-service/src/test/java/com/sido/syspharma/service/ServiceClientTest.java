// Déclare le package où se trouve cette classe de test.
// Conventionnellement, il reflète la structure du package du code testé (src/main/java).
package com.sido.syspharma.service;

// Imports des classes nécessaires pour ce test :
import com.sido.syspharma.dao.exceptions.DatabaseException; // Exception personnalisée pour les erreurs de base de données.
import com.sido.syspharma.dao.interfaces.IClientDAO; // Interface du DAO que nous allons mocker.
import com.sido.syspharma.domaine.enums.Role; // Enum pour les rôles des utilisateurs.
import com.sido.syspharma.domaine.exceptions.BusinessException; // Exception personnalisée pour les erreurs métier.
import com.sido.syspharma.domaine.model.Client; // Classe modèle (entité) que nous allons manipuler.

// Imports de JUnit 5 pour les annotations et fonctionnalités de test :
import org.junit.jupiter.api.BeforeEach; // Annotation pour une méthode à exécuter avant chaque test.
import org.junit.jupiter.api.DisplayName; // Annotation pour donner un nom descriptif au test/classe de test.
import org.junit.jupiter.api.Test; // Annotation pour marquer une méthode comme étant un cas de test.
import org.junit.jupiter.api.extension.ExtendWith; // Permet d'enregistrer des extensions JUnit (comme Mockito).

// Imports de Mockito pour la création de mocks et l'injection de dépendances :
import org.mockito.InjectMocks; // Annote un champ où les mocks doivent être injectés (la classe testée).
import org.mockito.Mock; // Annote un champ qui doit être un mock.
import org.mockito.junit.jupiter.MockitoExtension; // Extension JUnit 5 pour initialiser les mocks et @InjectMocks.

import java.sql.SQLException; // Importé pour pouvoir créer une instance de SQLException comme cause simulée.
import java.util.Arrays; // Classe utilitaire pour créer des listes à partir de tableaux.
import java.util.Collections; // Classe utilitaire pour les collections, notamment pour `emptyList()`.
import java.util.List; // Interface pour les listes.
import java.util.Optional; // Conteneur pour une valeur qui peut être absente.

// Imports statiques pour les méthodes d'assertion de JUnit et les méthodes statiques de Mockito.
// Cela permet d'écrire assertEquals(...) au lieu de Assertions.assertEquals(...), etc.
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) : Indique à JUnit 5 d'utiliser l'extension Mockito.
// Cette extension s'occupe d'initialiser les champs annotés avec @Mock et @InjectMocks.
@ExtendWith(MockitoExtension.class)
// @DisplayName : Donne un nom lisible à cette suite de tests, qui apparaîtra dans les rapports.
@DisplayName("Tests de la couche ServiceClient")
class ServiceClientTest { // Le nom de la classe de test se termine conventionnellement par "Test".

    // @Mock : Demande à Mockito de créer un objet mock (simulacre) de l'interface IClientDAO.
    // Cet objet mock ne contiendra pas de vraie logique d'accès à la base de données,
    // mais on pourra lui dire comment se comporter (ex: quoi retourner quand une méthode est appelée).
    @Mock
    private IClientDAO clientDAO;

    // @InjectMocks : Demande à Mockito de créer une instance réelle de ServiceClient
    // ET d'essayer d'injecter tous les champs annotés @Mock (ici, clientDAO)
    // dans cette instance de ServiceClient (typiquement via son constructeur ou des setters).
    // La classe ServiceClient doit avoir un constructeur ou un setter approprié pour que cela fonctionne.
    @InjectMocks
    private ServiceClient serviceClient;

    // Déclaration de variables d'instance pour les objets Client qui seront utilisés
    // dans plusieurs méthodes de test. Ils seront initialisés dans la méthode setUp().
    private Client clientValidePourTest;
    private Client clientValidePourTest2;

    // @BeforeEach : Cette méthode sera exécutée par JUnit AVANT CHAQUE méthode de test (@Test)
    // dans cette classe. Utile pour initialiser des objets communs ou réinitialiser un état.
    @BeforeEach
    void setUp() {
        // Crée une instance de Client avec des données valides pour les tests.
        // Utilise le constructeur qui prend tous les arguments nécessaires.
        clientValidePourTest = new Client("Doe", "John", "test@example.com", "123 Main St", "555-1234", "password123", Role.CLIENT);
        // Assigne un ID à cet objet client. Dans une vraie application, l'ID serait
        // généralement assigné par la base de données lors de l'insertion.
        clientValidePourTest.setId(1L);

        // Crée une deuxième instance de Client pour les tests qui manipulent des listes.
        clientValidePourTest2 = new Client("Smith", "Jane", "jane@example.com", "456 Oak Ave", "555-5678", "pass456", Role.CLIENT);
        clientValidePourTest2.setId(2L);
    }

    // --- Début de la section des tests pour la méthode creerCompte ---

    // @Test : Marque cette méthode comme un cas de test exécutable par JUnit.
    @Test
    // @DisplayName : Fournit un nom lisible pour ce cas de test spécifique.
    @DisplayName("creerCompte: Devrait créer un compte client avec succès")
    // La méthode de test peut déclarer les exceptions qu'elle s'attend à ce que le code testé lève (ou qu'elle propage).
    void creerCompte_Success() throws BusinessException, DatabaseException {
        // Arrange (Préparation) : Configurer le comportement du mock.
        // when(...) : Spécifie une condition (appel de méthode sur un mock).
        // clientDAO.inserer(any(Client.class)) : Lorsque la méthode 'inserer' du mock 'clientDAO'
        // est appelée avec n'importe quel objet de type Client (grâce à any(Client.class))...
        // thenReturn(true) : ...alors, le mock doit retourner 'true'.
        when(clientDAO.inserer(any(Client.class))).thenReturn(true);

        // Act (Action) : Exécuter la méthode de serviceClient que l'on veut tester.
        // On passe l'objet clientValidePourTest initialisé dans setUp().
        boolean result = serviceClient.creerCompte(clientValidePourTest);

        // Assert (Vérification) : Vérifier que le résultat est celui attendu.
        // assertTrue(boolean condition, String messageSiEchec) : Vérifie que la condition est vraie.
        assertTrue(result, "La création de compte devrait retourner true");
        // verify(mockObject, times(int)) : Vérifie qu'une méthode d'un mock a été appelée un certain nombre de fois.
        // clientDAO : Le mock à vérifier.
        // times(1) : On s'attend à ce que la méthode ait été appelée exactement une fois.
        // .inserer(clientValidePourTest) : La méthode spécifique et l'argument exact attendu.
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    @Test
    @DisplayName("creerCompte: Devrait retourner false si l'insertion DAO échoue (retourne false)")
    void creerCompte_DAOReturnsFalse() throws BusinessException, DatabaseException {
        // Arrange : Configurer le mock pour simuler un échec "non exceptionnel" du DAO.
        when(clientDAO.inserer(any(Client.class))).thenReturn(false);

        // Act
        boolean result = serviceClient.creerCompte(clientValidePourTest);

        // Assert : S'attendre à ce que le service retourne false.
        assertFalse(result, "La création de compte devrait retourner false si le DAO retourne false");
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    @Test
    @DisplayName("creerCompte: Devrait lancer BusinessException si DatabaseException lors de l'insertion")
    void creerCompte_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // Arrange : Configurer le mock pour qu'il lance une DatabaseException.
        // On crée une instance de SQLException pour simuler une cause d'erreur bas niveau.
        SQLException causeSimulee = new SQLException("Erreur SQL d'insertion simulée");
        // On crée une instance de DatabaseException, en lui passant la SQLException comme cause.
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors de l'insertion", causeSimulee);
        // doThrow(exception).when(mock).method(...) : Syntaxe pour dire au mock de lancer une exception.
        doThrow(dbExceptionSimulee).when(clientDAO).inserer(any(Client.class));

        // Act & Assert : On s'attend à ce que l'appel à serviceClient.creerCompte lance une BusinessException.
        // assertThrows(ClasseExceptionAttendue.class, Executable, String messageSiEchec) :
        // Vérifie qu'une exception du type attendu est lancée par l'exécutable (fourni par une lambda).
        // Retourne l'exception lancée pour des vérifications supplémentaires.
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.creerCompte(clientValidePourTest);
        }, "Devrait lancer BusinessException");

        // Vérifications supplémentaires sur l'exception attrapée :
        // S'assurer que le message de la BusinessException contient un texte pertinent.
        assertTrue(thrown.getMessage().contains("Erreur lors de la création du client"), "Le message de BusinessException devrait indiquer une erreur de création.");
        // S'assurer que la BusinessException a bien une cause (l'exception originale du DAO).
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        // S'assurer que la cause de la BusinessException est bien l'instance de DatabaseException que nous avons simulée.
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        // Vérifier que la méthode du DAO a quand même été appelée.
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    // --- Début de la section des tests pour la méthode seConnecter ---

    @Test
    @DisplayName("seConnecter: Devrait permettre la connexion avec des identifiants corrects")
    void seConnecter_Success() throws BusinessException, DatabaseException {
        // Arrange : Configurer le mock clientDAO.
        // Quand trouverParEmail est appelé avec l'email de clientValidePourTest...
        // ...retourner un Optional contenant clientValidePourTest (simule que le client existe).
        when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));

        // Act : Appeler la méthode seConnecter du service.
        // La méthode seConnecter de votre ServiceClient retourne un boolean.
        boolean result = serviceClient.seConnecter(clientValidePourTest.getEmail(), clientValidePourTest.getPassword());

        // Assert : Vérifier que la connexion a réussi.
        assertTrue(result, "La connexion devrait être réussie et retourner true");
        // Vérifier que la méthode trouverParEmail du DAO a été appelée.
        verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
    }

    @Test
    @DisplayName("seConnecter: Devrait refuser la connexion avec un mot de passe incorrect")
    void seConnecter_WrongPassword() throws BusinessException, DatabaseException {
        // Arrange : Simuler que le client est trouvé par email.
        when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));

        // Act : Tenter de se connecter avec un mot de passe incorrect.
        boolean result = serviceClient.seConnecter(clientValidePourTest.getEmail(), "motdepasseIncorrect");

        // Assert : Vérifier que la connexion a échoué.
        assertFalse(result, "La connexion devrait échouer et retourner false avec un mot de passe incorrect");
        verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
    }

    @Test
    @DisplayName("seConnecter: Devrait refuser la connexion si l'email n'est pas trouvé")
    void seConnecter_EmailNotFound() throws BusinessException, DatabaseException {
        // Arrange : Simuler que l'email n'est pas trouvé dans la base de données.
        // Le mock retourne un Optional vide.
        when(clientDAO.trouverParEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act : Tenter de se connecter avec cet email inconnu.
        boolean result = serviceClient.seConnecter("unknown@example.com", "anypassword");

        // Assert : Vérifier que la connexion a échoué.
        assertFalse(result, "La connexion devrait échouer et retourner false si l'email n'est pas trouvé");
        verify(clientDAO, times(1)).trouverParEmail("unknown@example.com");
    }

    @Test
    @DisplayName("seConnecter: Devrait lancer BusinessException si DatabaseException lors de la recherche")
    void seConnecter_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // Arrange : Simuler que le DAO lance une DatabaseException lors de la recherche.
        SQLException causeSimulee = new SQLException("Erreur SQL de recherche simulée");
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors de la recherche par email", causeSimulee);
        // anyString() : correspond à n'importe quel argument de type String.
        doThrow(dbExceptionSimulee).when(clientDAO).trouverParEmail(anyString());

        // Act & Assert : S'attendre à ce qu'une BusinessException soit lancée.
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.seConnecter("any@email.com", "anypassword");
        }, "Devrait lancer BusinessException");

        assertTrue(thrown.getMessage().contains("Connexion impossible"), "Le message de BusinessException devrait indiquer une impossibilité de connexion.");
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        verify(clientDAO, times(1)).trouverParEmail(anyString());
    }

    // --- Début de la section des tests pour la méthode getTousLesClients ---

    @Test
    @DisplayName("getTousLesClients: Devrait retourner une liste de clients")
    void getTousLesClients_Success() throws BusinessException, DatabaseException {
        // Arrange : Configurer le mock pour retourner une liste de clients.
        // Arrays.asList(...) crée une liste fixe à partir des éléments fournis.
        when(clientDAO.listerTous()).thenReturn(Arrays.asList(clientValidePourTest, clientValidePourTest2));

        // Act : Appeler la méthode du service.
        List<Client> clients = serviceClient.getTousLesClients();

        // Assert : Vérifier la liste retournée.
        assertNotNull(clients, "La liste des clients ne devrait pas être null");
        assertEquals(2, clients.size(), "La liste devrait contenir 2 clients");
        // Vérifier que les clients attendus sont présents dans la liste.
        assertTrue(clients.contains(clientValidePourTest), "La liste devrait contenir le premier client de test.");
        assertTrue(clients.contains(clientValidePourTest2), "La liste devrait contenir le deuxième client de test.");
        verify(clientDAO, times(1)).listerTous();
    }

    @Test
    @DisplayName("getTousLesClients: Devrait retourner une liste vide si aucun client n'est trouvé")
    void getTousLesClients_EmptyList() throws BusinessException, DatabaseException {
        // Arrange : Configurer le mock pour retourner une liste vide.
        // Collections.emptyList() retourne une liste immuable vide.
        when(clientDAO.listerTous()).thenReturn(Collections.emptyList());

        // Act
        List<Client> clients = serviceClient.getTousLesClients();

        // Assert
        assertNotNull(clients, "La liste des clients ne devrait pas être null même si vide");
        assertTrue(clients.isEmpty(), "La liste des clients devrait être vide");
        verify(clientDAO, times(1)).listerTous();
    }

    @Test
    @DisplayName("getTousLesClients: Devrait lancer BusinessException si DatabaseException survient lors du listage")
    void getTousLesClients_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // Arrange : Simuler que le DAO lance une DatabaseException.
        SQLException causeSimulee = new SQLException("Erreur SQL de listage simulée");
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors du listage", causeSimulee);
        doThrow(dbExceptionSimulee).when(clientDAO).listerTous();

        // Act & Assert : S'attendre à ce qu'une BusinessException soit lancée.
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.getTousLesClients();
        }, "Devrait lancer BusinessException");

        assertTrue(thrown.getMessage().contains("Erreur de récupération des clients"), "Le message de BusinessException devrait indiquer une erreur de récupération.");
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        verify(clientDAO, times(1)).listerTous();
    }
}