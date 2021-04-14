
package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.player.Adder;

public abstract class Marble {
    
    public abstract boolean onDraw(Adder var1) throws MatchEndedException;
}
