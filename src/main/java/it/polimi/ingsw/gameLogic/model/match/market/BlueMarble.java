package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.model.match.player.Adder;

/**
 * This class implements the blue marble.
 */
public class BlueMarble extends Marble {

    /**
     * This method create a PhysicalResource with type SHIELD and quantity 1 and it adds this resource to the player's
     * marketBuffer.
     * If the PhysicalResource throws a NegativeQuantityException this method shuts down the program
     * @param adder the player's interface
     * @return false
     */
    public boolean onDraw(Adder adder) {
        try {
            adder.addToWarehouse(new PhysicalResource(ResType.SHIELD, 1));
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
        return "BlueMarble";
    }
}
