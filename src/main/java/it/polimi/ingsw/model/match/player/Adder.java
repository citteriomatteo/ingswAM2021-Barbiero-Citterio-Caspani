package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;

/*
    Interface used for limiting access to player methods
    It contains methods for adding resources or faith points
*/
public interface Adder  {

    //This method increments player's faithPoints
    boolean addFaithPoints(int quantity) throws MatchEndedException;

    //This method adds resource to the strong box
    boolean addToStrongBox(PhysicalResource resource);

    //Add the resource to the warehouse
    boolean addToWarehouse(PhysicalResource resource);
}
