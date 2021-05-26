package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.exceptions.LastRoundException;

/*
    Interface used for limiting access to player methods
    It contains methods for adding resources or faith points
*/  //todo ALL JAVADOC
public interface Adder  {

    //This method increments player's faithPoints
    boolean addFaithPoints(int quantity) throws LastRoundException;

    //This method adds resource to the strong box
    boolean addToStrongBox(PhysicalResource resource);

    //Add the resource to the warehouse
    boolean addToWarehouse(PhysicalResource resource);
}
