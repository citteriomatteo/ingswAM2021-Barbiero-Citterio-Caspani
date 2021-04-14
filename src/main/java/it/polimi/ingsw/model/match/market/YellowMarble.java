package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class YellowMarble extends Marble {
    public YellowMarble() {
    }

    public boolean onDraw(Adder adder) {
        try {
            adder.addToWarehouse(new PhysicalResource(ResType.COIN, 1));
        } catch (NegativeQuantityException e) {
            e.printStackTrace(); System.err.println("Application shutdown due to an internal error.");
            System.exit(1);
        }
        return false;
    }

    public String toString() {
        return "YellowMarble";
    }
}
