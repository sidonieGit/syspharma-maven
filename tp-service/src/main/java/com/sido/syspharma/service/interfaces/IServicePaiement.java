package com.sido.syspharma.service.interfaces;

import com.sido.syspharma.domaine.commande.Commande;
import com.sido.syspharma.domaine.commande.Paiement;
import com.sido.syspharma.domaine.enums.ModePaiement;

public interface IServicePaiement {
    Paiement effectuerPaiement(Commande commande, ModePaiement modePaiement);

    double calculerMontant(Commande commande);
}
