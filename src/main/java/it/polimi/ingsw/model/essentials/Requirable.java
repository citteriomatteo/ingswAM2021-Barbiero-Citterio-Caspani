package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

public interface Requirable {

    //this method verify the presence of the cards or of the PhysicalResources on the playerBoard
    boolean verify(Verificator verificator);
}
