package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.match.player.Adder;

/**
 * This class implements the white marble.
 */

public class WhiteMarble extends Marble {
    /**
     * this method simply returns true
     * @param adder the player's interface
     * @return true
     */
    public boolean onDraw(Adder adder) {
        return true;
    }

    /**
     * Redefinition of class' toString
     * @return a string
     */
    public String toString() {
        return "WhiteMarble";
    }
}