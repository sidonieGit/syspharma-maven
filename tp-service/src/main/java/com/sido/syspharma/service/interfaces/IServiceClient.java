package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.domaine.exceptions.BusinessException;

import java.util.List;

public interface IServiceClient {
    boolean creerCompte(Client client) throws BusinessException;

    boolean seConnecter(String email, String motDePasse) throws BusinessException;

    List<Client> getTousLesClients() throws BusinessException;
}
