package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

public interface Requirable {

    //this method verify the presence of the cards or the PhisicalResources for the player
    boolean verify(Verificator verificator);
}
