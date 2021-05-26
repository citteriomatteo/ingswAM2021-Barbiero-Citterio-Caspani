package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.match.player.Adder;
//todo JAVADOC
public class WhiteMarble extends Marble {
    /**
     * this method simply returns true
     * @param adder the player's interface
     * @return true
     */
    public boolean onDraw(Adder adder) {
        return true;
    }

    public String toString() {
        return "WhiteMarble";
    }
}