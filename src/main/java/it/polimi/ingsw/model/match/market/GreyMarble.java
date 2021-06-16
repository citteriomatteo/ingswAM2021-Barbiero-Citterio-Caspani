
package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

/**
 * This class implements the grey marble.
 */

public class GreyMarble extends Marble {

    /**
     * This method create a PhysicalResource with type STONE and quantity 1 and it adds this resource to the player's
     * marketBuffer.
     * If the PhysicalResource throws a NegativeQuantityException this method shuts down the program
     * @param adder the player's interface
     * @return false
     */
    public boolean onDraw(Adder adder) {
        try {
            adder.addToWarehouse(new PhysicalResource(ResType.STONE, 1));
        } catch (NegativeQuantityException e) {
            e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+".");
            System.exit(1);
        }
        return false;
    }

    /**
     * Redefinition of class' toString
     * @return a string
     */
    public String toString() {
        return "GreyMarble";
    }
}
