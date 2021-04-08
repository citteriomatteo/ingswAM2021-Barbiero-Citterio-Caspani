package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.exceptions.ShelfInsertException;

import java.util.Map;

public interface WarehouseDecorator extends Warehouse
{
    PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException, NegativeQuantityException;

    //inserts the resource into the chosen shelf.
    boolean moveInShelf(PhysicalResource res, int shelf) throws ShelfInsertException, NegativeQuantityException, InvalidOperationException;

    //receives the resource to remove from the marketBuffer, and does it one by one.
    boolean cleanMarketBuffer(PhysicalResource res) throws NegativeQuantityException;

    //returns the current warehouse mapped state.
    Map<ResType, Integer> getWarehouse();

    //switches two shelves' content.
    boolean switchShelf(int shelf1, int shelf2) throws ShelfInsertException, NegativeQuantityException, InvalidOperationException, NotEnoughResourcesException;

}
