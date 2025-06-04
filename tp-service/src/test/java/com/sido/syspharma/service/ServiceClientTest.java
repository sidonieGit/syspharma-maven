package com.sido.syspharma.service;

import com.sido.syspharma.dao.exceptions.DatabaseException;
import com.sido.syspharma.dao.interfaces.IClientDAO;
import com.sido.syspharma.domaine.enums.Role;
import com.sido.syspharma.domaine.exceptions.BusinessException;
import com.sido.syspharma.domaine.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException; // Importer pour simuler une cause réelle
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de la couche ServiceClient")
class ServiceClientTest {

    @Mock
    private IClientDAO clientDAO;

    @InjectMocks
    private ServiceClient serviceClient;

    private Client clientValidePourTest;
    private Client clientValidePourTest2;

    @BeforeEach
    void setUp() {
        // Utilisation du constructeur complet de Client
        clientValidePourTest = new Client("Doe", "John", "test@example.com", "123 Main St", "555-1234", "password123", Role.CLIENT);
        clientValidePourTest.setId(1L);

        clientValidePourTest2 = new Client("Smith", "Jane", "jane@example.com", "456 Oak Ave", "555-5678", "pass456", Role.CLIENT);
        clientValidePourTest2.setId(2L);
    }

    // --- Tests pour creerCompte ---

    @Test
    @DisplayName("creerCompte: Devrait créer un compte client avec succès")
    void creerCompte_Success() throws BusinessException, DatabaseException {
        when(clientDAO.inserer(any(Client.class))).thenReturn(true);

        boolean result = serviceClient.creerCompte(clientValidePourTest);

        assertTrue(result, "La création de compte devrait retourner true");
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    @Test
    @DisplayName("creerCompte: Devrait retourner false si l'insertion DAO échoue (retourne false)")
    void creerCompte_DAOReturnsFalse() throws BusinessException, DatabaseException {
        when(clientDAO.inserer(any(Client.class))).thenReturn(false);

        boolean result = serviceClient.creerCompte(clientValidePourTest);

        assertFalse(result, "La création de compte devrait retourner false si le DAO retourne false");
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    @Test
    @DisplayName("creerCompte: Devrait lancer BusinessException si DatabaseException lors de l'insertion")
    void creerCompte_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // CORRECTION : Création de DatabaseException avec une cause
        SQLException causeSimulee = new SQLException("Erreur SQL d'insertion simulée");
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors de l'insertion", causeSimulee);
        doThrow(dbExceptionSimulee).when(clientDAO).inserer(any(Client.class));

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.creerCompte(clientValidePourTest);
        }, "Devrait lancer BusinessException");

        assertTrue(thrown.getMessage().contains("Erreur lors de la création du client"), "Le message de BusinessException devrait indiquer une erreur de création.");
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        verify(clientDAO, times(1)).inserer(clientValidePourTest);
    }

    // --- Tests pour seConnecter ---

    @Test
    @DisplayName("seConnecter: Devrait permettre la connexion avec des identifiants corrects")
    void seConnecter_Success() throws BusinessException, DatabaseException {
        when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));

        // CORRECTION: seConnecter retourne boolean
        boolean result = serviceClient.seConnecter(clientValidePourTest.getEmail(), clientValidePourTest.getPassword());

        assertTrue(result, "La connexion devrait être réussie et retourner true");
        verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
    }

    @Test
    @DisplayName("seConnecter: Devrait refuser la connexion avec un mot de passe incorrect")
    void seConnecter_WrongPassword() throws BusinessException, DatabaseException {
        when(clientDAO.trouverParEmail(clientValidePourTest.getEmail())).thenReturn(Optional.of(clientValidePourTest));

        // CORRECTION: seConnecter retourne boolean
        boolean result = serviceClient.seConnecter(clientValidePourTest.getEmail(), "motdepasseIncorrect");

        assertFalse(result, "La connexion devrait échouer et retourner false avec un mot de passe incorrect");
        verify(clientDAO, times(1)).trouverParEmail(clientValidePourTest.getEmail());
    }

    @Test
    @DisplayName("seConnecter: Devrait refuser la connexion si l'email n'est pas trouvé")
    void seConnecter_EmailNotFound() throws BusinessException, DatabaseException {
        when(clientDAO.trouverParEmail("unknown@example.com")).thenReturn(Optional.empty());

        // CORRECTION: seConnecter retourne boolean
        boolean result = serviceClient.seConnecter("unknown@example.com", "anypassword");

        assertFalse(result, "La connexion devrait échouer et retourner false si l'email n'est pas trouvé");
        verify(clientDAO, times(1)).trouverParEmail("unknown@example.com");
    }

    @Test
    @DisplayName("seConnecter: Devrait lancer BusinessException si DatabaseException lors de la recherche")
    void seConnecter_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // CORRECTION : Création de DatabaseException avec une cause
        SQLException causeSimulee = new SQLException("Erreur SQL de recherche simulée");
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors de la recherche par email", causeSimulee);
        doThrow(dbExceptionSimulee).when(clientDAO).trouverParEmail(anyString());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.seConnecter("any@email.com", "anypassword");
        }, "Devrait lancer BusinessException");

        assertTrue(thrown.getMessage().contains("Connexion impossible"), "Le message de BusinessException devrait indiquer une impossibilité de connexion.");
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        verify(clientDAO, times(1)).trouverParEmail(anyString());
    }

    // --- Tests pour getTousLesClients ---

    @Test
    @DisplayName("getTousLesClients: Devrait retourner une liste de clients")
    void getTousLesClients_Success() throws BusinessException, DatabaseException {
        when(clientDAO.listerTous()).thenReturn(Arrays.asList(clientValidePourTest, clientValidePourTest2));

        List<Client> clients = serviceClient.getTousLesClients();

        assertNotNull(clients, "La liste des clients ne devrait pas être null");
        assertEquals(2, clients.size(), "La liste devrait contenir 2 clients");
        assertTrue(clients.contains(clientValidePourTest), "La liste devrait contenir le premier client de test.");
        assertTrue(clients.contains(clientValidePourTest2), "La liste devrait contenir le deuxième client de test.");
        verify(clientDAO, times(1)).listerTous();
    }

    @Test
    @DisplayName("getTousLesClients: Devrait retourner une liste vide si aucun client n'est trouvé")
    void getTousLesClients_EmptyList() throws BusinessException, DatabaseException {
        when(clientDAO.listerTous()).thenReturn(Collections.emptyList());

        List<Client> clients = serviceClient.getTousLesClients();

        assertNotNull(clients, "La liste des clients ne devrait pas être null même si vide");
        assertTrue(clients.isEmpty(), "La liste des clients devrait être vide");
        verify(clientDAO, times(1)).listerTous();
    }

    @Test
    @DisplayName("getTousLesClients: Devrait lancer BusinessException si DatabaseException survient lors du listage")
    void getTousLesClients_ThrowsBusinessException_When_DAOThrowsDatabaseException() throws DatabaseException {
        // CORRECTION : Création de DatabaseException avec une cause
        SQLException causeSimulee = new SQLException("Erreur SQL de listage simulée");
        DatabaseException dbExceptionSimulee = new DatabaseException("Erreur interne du DAO lors du listage", causeSimulee);
        doThrow(dbExceptionSimulee).when(clientDAO).listerTous();

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            serviceClient.getTousLesClients();
        }, "Devrait lancer BusinessException");

        assertTrue(thrown.getMessage().contains("Erreur de récupération des clients"), "Le message de BusinessException devrait indiquer une erreur de récupération.");
        assertNotNull(thrown.getCause(), "BusinessException devrait avoir une cause.");
        assertSame(dbExceptionSimulee, thrown.getCause(), "La cause de BusinessException devrait être la DatabaseException simulée.");
        verify(clientDAO, times(1)).listerTous();
    }
}