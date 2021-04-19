package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class YellowMarble extends Marble {

    /**
     * This method create a PhysicalResource with type COIN and quantity 1 and it adds this resource to the player's
     * marketBuffer.
     * If the PhysicalResource throws a NegativeQuantityException this method shuts down the program
     * @param adder the player's interface
     * @return false
     */

    public boolean onDraw(Adder adder) {
        try {
            adder.addToWarehouse(new PhysicalResource(ResType.COIN, 1));
        } catch (NegativeQuantityException e) {
            e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+".");
            System.exit(1);
        }
        return false;
    }

    public String toString() {
        return "YellowMarble";
    }
}
