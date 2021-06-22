package it.polimi.ingsw.gameLogic.model.match.player;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;

/**
    Interface used for limiting access to player methods
    It contains methods for adding resources or faith points
*/
public interface Adder  {

    /**
     *  This method increments player's faithPoints.
     * @param quantity the quantity of faith points.
     * @return a boolean
     * @throws LastRoundException if the player has reached the end of the path.
     */
    boolean addFaithPoints(int quantity) throws LastRoundException;

    /**
     * This method adds resource to the strong box.
     * @param resource the single resource to add
     * @return a boolean
     */
    boolean addToStrongBox(PhysicalResource resource);

    /**
     * This method adds the resource to the warehouse.
     * @param resource the resource to add
     * @return a boolean
     */
    boolean addToWarehouse(PhysicalResource resource);
}
