package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.player.Adder;

/**
 * This interface unifies physical resources and faith points (for cards earnings).
 */
public interface Resource {

    /**
     * Adds the resource to the StrongBox or increments the faitMarker
     * @param adder the player's interface
     * @return true if the resource was added or the faitMarker was incremented
     * @throws LastRoundException if the player reaches the end of his FaithPath
     */
    boolean add(Adder adder) throws LastRoundException;

    /**
     * Returns true if this resource is physical
     * @return true if this resource is physical, false if this is a faith point
     */
    boolean isPhysical();

    /**
     * Returns the quantity of the resource
     * @return the quantity of the resource
     */
    int getQuantity();
}
