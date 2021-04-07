package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.PhysicalResource;

/*
    Interface used for limiting access to player methods
    It contains methods for adding resources or faith points
*/
public interface Adder  {

    //This method increments player's faithPoints
    boolean addFaithPoints(int quantity);

    //This method adds resource to the strong box
    boolean addToStrongBox(PhysicalResource resource);

    //Add the resource to the warehouse
    boolean addToWarehouse(PhysicalResource resource);
}
