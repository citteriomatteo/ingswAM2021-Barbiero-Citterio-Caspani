package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class WhiteMarble extends Marble {
    public WhiteMarble() {
    }

    public boolean onDraw(Adder adder) {
        return true;
    }

    public String toString() {
        return "WhiteMarble";
    }
}