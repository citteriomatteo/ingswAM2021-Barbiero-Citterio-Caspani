package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

public interface Requirable {

    /**
     * This method verify the presence of this Requirable on the PlayerBoard of the player passed
     * @param verificator the player
     * @return true if this Requirable is present on the verificator's PlayerBoard
     */
    boolean verify(Verificator verificator);
}
