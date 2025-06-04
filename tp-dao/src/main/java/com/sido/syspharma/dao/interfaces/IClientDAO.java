package com.sido.syspharma.dao.interfaces;

import com.sido.syspharma.domaine.model.Client;
import com.sido.syspharma.dao.exceptions.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface IClientDAO {


    boolean inserer(Client client) throws DatabaseException;

    Optional<Client> trouverParEmail(String email) throws DatabaseException;

    List<Client> listerTous() throws DatabaseException;
}
