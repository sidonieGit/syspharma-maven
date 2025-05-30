package com.sido.syspharma.dao.interfaces;

import com.sido.syspharma.domaine.model.Compte;
import com.sido.syspharma.exceptions.DatabaseException;

public interface ICompteDAO {

    Compte findByEmail(String email) throws DatabaseException;

    boolean update(Compte compte) throws DatabaseException;

    // éventuellement : insert(Compte compte), delete(Compte compte)...
}
