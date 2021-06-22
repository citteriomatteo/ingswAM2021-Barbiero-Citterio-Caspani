
package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.player.Adder;

/**
 * This abstract class is made common for every marble type.
 */

public abstract class Marble {
    /**
     * This method adds a single resource or a single faithPoint to the passed player
     * @param adder the player's interface
     * @return true only if the marble is white
     * @throws LastRoundException if the player reaches the end of his FaithPath
     */
    public abstract boolean onDraw(Adder adder) throws LastRoundException;
}
