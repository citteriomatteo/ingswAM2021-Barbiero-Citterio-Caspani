package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.player.Adder;

/**
 * This class implements the red marble.
 */

public class RedMarble extends Marble {

    /**
     * This method adds a single faithPoint to the player's FaithPath
     * @param adder the player's interface
     * @return false
     * @throws LastRoundException if the player reaches the end of his FaithPath
     */
    public boolean onDraw(Adder adder) throws LastRoundException {
        adder.addFaithPoints(1);
        return false;
    }

    /**
     * Redefinition of class' toString
     * @return a string
     */
    public String toString() { return "RedMarble"; }
}
