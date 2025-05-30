package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.model.Statistique;

import java.time.LocalDate;
import java.util.List;

public interface IServiceStatistique {
    Statistique calculerStatistiques(List<Commande> commandes, LocalDate debut, LocalDate fin);
}
