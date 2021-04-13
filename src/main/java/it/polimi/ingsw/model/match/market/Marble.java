
package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public abstract class Marble {
    public Marble() {
    }

    public abstract boolean onDraw(Adder var1) throws NegativeQuantityException, MatchEndedException;
}
