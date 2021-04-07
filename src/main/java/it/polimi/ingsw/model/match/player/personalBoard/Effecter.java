package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;

// Effecter interface is used to limit controller's access into the personal board.
public interface Effecter
{
    /*
    This method removes the activated leader from handLeader list and adds it to the activeLeaders list.
    Eventually adds it to the ProductionActiveLeader list, if it has a production effect.
     */
    boolean addActiveLeader(LeaderCard lc);

    /*
    This method evolves the warehouse structure (updating the new shelves),
    according to the Decorator pattern we use.
     */
    boolean warehouseEvolution(PhysicalResource resource);

    //This method adds a new white-marble conversion to the WhiteMarbleConversions list.
    boolean addNewConversion(PhysicalResource resource);

    /*
    This method calls the function with the same name, contained in DiscountMap class.
    Basically, it inserts a new discount for the next operations, and setDiscount receives it as a resource.
     */
    boolean setDiscount(PhysicalResource resource);
}