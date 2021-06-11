package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.exceptions.ShelfInsertException;

import java.util.*;

/**
 * This interface is a piece of Decorator pattern implementation.
 * Implemented by Warehouse.
 */
public interface Warehouse
{
    /**
     * @param shelf         the chosen shelf
     * @param numResources  the number of resources to take
     * @return              the requested resource
     * @throws NotEnoughResourcesException whenever the resource is not available on the shelf
     * @see ConcreteWarehouse
     * @see ExtraShelfWarehouse
     */
    PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException;

    /**
     * Moves in the specified shelf, if there is enough space or an EMPTY (quantity=0) shelf with whatever previous ResType in it.
     * Before moving, checks for "res" presence in the market buffer.
     * @throws ShelfInsertException for every problem space-or-type related
     * @throws InvalidOperationException whenever the received resource is not present in the marketBuffer first.
     * @param res   the resource to try to insert into the shelf (the same resource MUST be present in the market buffer before)
     * @param shelf the number of the shelf, starting from 1.
     * @return      true
     */
    boolean moveInShelf(PhysicalResource res, int shelf) throws ShelfInsertException, InvalidQuantityException;

    /**
     * Receives the resource to remove from the marketBuffer, and does it one by one.
     * @param res the resource to remove from the marketBuffer
     * @return    true
     */
    boolean cleanMarketBuffer(PhysicalResource res);

    /**
     * This method returns the shelves List, converted to a Map.
     * @return an HashMap version of the warehouse status.
     */
    Map<ResType, Integer> getWarehouse();

    /**
     * This method returns the perfect disposition of the warehouse.
     * @return an ArrayList representing the resources disposition in the warehouse.
     */
    List<PhysicalResource> getWarehouseDisposition();

    /**
     * The method checks for the availability of the space in both resources before switching shelves,
     * avoiding the Undo procedure, then switches resources between the two shelves.
     * @throws ShelfInsertException when the switch is impossible due to resources quantities.
     * @param shelf1 the first shelf
     * @param shelf2 the second shelf
     * @return       true
     */
    boolean switchShelf(int shelf1, int shelf2) throws InvalidOperationException;

    /**
     * @return the market buffer.
     */
    List<PhysicalResource> getBuffer();

    /**
     * This method, eventually, splits the received resource in more single quantities and inserts them in the marketBuffer.
     * @param res the resource to insert in the buffer
     * @return    true
     */
    boolean marketDraw(PhysicalResource res);

    /**
     * This method clears marketBuffer and returns its previous size, for penalty faith points purposes.
     * @return the remaining size of the buffer
     */
    int discardRemains() throws InvalidOperationException;

    /**
     * This method returns the quantity of resources of the requested type.
     * @param  type the type to count.
     * @return the number of type occurrences.
     */
    int getNumberOf(ResType type);

    /**
     * This method returns the size of the requested shelf.
     * @param shelf the shelf to analyze
     * @return      the shelf size
     */
    int getShelfSize(int shelf);

    /**
     * This method clones the current Warehouse.
     */
    static void clone(Warehouse wh, Warehouse clonedWh){
        wh.getWarehouseDisposition().stream().forEach(clonedWh::marketDraw);
        List<PhysicalResource> disposition = wh.getWarehouseDisposition();
        for(int i=0; i<3; i++)
            try{ clonedWh.moveInShelf(disposition.get(i), i+1); }
            catch(InvalidQuantityException | ShelfInsertException ignored){ }

    }
}