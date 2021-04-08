
package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class GreyMarble extends Marble {
    public GreyMarble() {
    }

    public boolean onDraw(Adder adder) throws NegativeQuantityException {
        adder.addToWarehouse(new PhysicalResource(ResType.STONE, 1));
        return false;
    }

    public String toString() {
        return "GreyMarble";
    }
}
