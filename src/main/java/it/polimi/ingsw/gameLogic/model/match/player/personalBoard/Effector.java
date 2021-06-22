package it.polimi.ingsw.gameLogic.model.match.player.personalBoard;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.leader.LeaderCard;

/** Effector interface is used to limit controller's access into the personal board. */
public interface Effector
{
    /**
     * This method inserts the new leader into the activeLeaders list and,
     * if it's a Production one, also in the activeProductionLeaders list.
     * @param newLeader     is the new leader
     * @param hasProduction is a boolean identifying if it is a Production one or not
     * @return              true
     */
    boolean addActiveLeader(LeaderCard newLeader, boolean hasProduction);

    /*
    This method evolves the warehouse structure (updating the new shelves),
    according to the Decorator pattern used.
    Warehouse wh = new ConcreteWarehouse()
                   -> new ExtraShelfWarehouse(...) (that points to the Concrete)
                    -> new ExtraShelfWarehouse(...) (that points to the first ExtraShelf).
     */
    /**
     * This method evolves the warehouse onto a new ExtraShelf version.
     * The new shelf define procedure is made by the ExtraShelfWarehouse constructor.
     * @param extraShelf is the resource that has all the informations about the new shelf (quantity and type)
     * @return           true
     */
    boolean warehouseEvolution(PhysicalResource extraShelf);

    /**
     * This method inserts a new white-marble conversion in the whiteMarbleConversions list.
     * @param conversion is the new conversion to insert
     * @return           the operation on-finish flag
     */
    boolean addNewConversion(PhysicalResource conversion);

    /**
     * This method inserts a new discount (the Resource defines it) in the discountMap.
     * @param discount defines the new discount
     * @return         true
     */
    boolean setDiscount(PhysicalResource discount);
}
