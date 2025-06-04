package com.sido.syspharma.dao.interfaces;

import com.sido.syspharma.domaine.model.Medicament;
import com.sido.syspharma.dao.exceptions.DatabaseException;

import java.util.List;

public interface IMedicamentDAO {
    boolean insererMedicament(Medicament medicament) throws DatabaseException;
    List<Medicament> rechercherParNom(String nom) throws DatabaseException;
}
