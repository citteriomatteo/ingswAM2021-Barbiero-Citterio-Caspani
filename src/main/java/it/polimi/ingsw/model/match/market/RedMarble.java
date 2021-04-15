package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.player.Adder;

public class RedMarble extends Marble {

    /**
     * This method adds a single faithPoint to the player's FaithPath
     * @param adder the player's interface
     * @return false
     * @throws MatchEndedException if the player reaches the end of his FaithPath
     */

    public boolean onDraw(Adder adder) throws MatchEndedException
    {
        adder.addFaithPoints(1);
        return false;
    }

    public String toString() {
        return "RedMarble";
    }
}
