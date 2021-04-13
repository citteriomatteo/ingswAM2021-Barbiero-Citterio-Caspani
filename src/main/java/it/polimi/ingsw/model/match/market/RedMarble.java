package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class RedMarble extends Marble {
    public RedMarble() {
    }

    public boolean onDraw(Adder adder) throws MatchEndedException {
        adder.addFaithPoints(1);
        return false;
    }

    public String toString() {
        return "RedMarble";
    }
}
