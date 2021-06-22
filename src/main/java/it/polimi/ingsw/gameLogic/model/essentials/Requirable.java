package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.model.match.player.Verificator;

/**
 * This interface unifies CardTypes and resources (for cards activation).
 */
public interface Requirable {

    /**
     * This method verifies the presence of this Requirable on the PlayerBoard of the player passed
     * @param verificator the player
     * @return true if this Requirable is present on the verificator's PlayerBoard
     */
    boolean verify(Verificator verificator);

    /**
     * This method returns true if the Requirable object is a resource, else false.
     * @return boolean
     */
    boolean isAResource();
}
