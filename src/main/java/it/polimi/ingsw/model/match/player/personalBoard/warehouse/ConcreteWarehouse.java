package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.exceptions.ShelfInsertException;

import java.util.*;

public class ConcreteWarehouse implements Warehouse
{
    private final List<PhysicalResource> shelves;
    private final List<PhysicalResource> marketBuffer;

    //constructor initializes the three shelves with UNKNOWN type and zero quantity.
    public ConcreteWarehouse()
    {
        shelves = new ArrayList<>();
        try {
            shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
            shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
            shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
        }
        catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); System.exit(1);}

        marketBuffer = new ArrayList<>();
    }

    /**
     * Returns the PhysicalResource in the chosen quantity, if possible, and removes it from the shelf.
     * @param shelf         the number of the shelf, starting from 1
     * @param numResources  the amount of resources to take from the specified shelf
     * @return              the resource taken from the shelf.
     */
    @Override
    public PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException
    {
        shelf--;
        int available= shelves.get(shelf).getQuantity();
        if(numResources > available)
            throw new NotEnoughResourcesException("Not enough resources in shelf "+shelf+"!");
        try
        {
            PhysicalResource newshelf = new PhysicalResource(shelves.get(shelf).getType(), (available - numResources));
            shelves.set(shelf, newshelf);
        }
        catch(NegativeQuantityException e)
        { e.printStackTrace(); System.err.println("Application shutdown due to an internal error.."); System.exit(1);}
        PhysicalResource res=null;
        try { res = new PhysicalResource(shelves.get(shelf).getType(), numResources); }
        catch(NegativeQuantityException e)
        { e.printStackTrace(); System.err.println("Application shutdown due to an internal error.."); System.exit(1);}
        return res;
    }


    /**
     * Moves in the specified shelf, if there is enough space or an EMPTY (quantity=0) shelf with whatever previous ResType in it.
     * Before moving, checks for "res" presence in the market buffer.
     * @throws ShelfInsertException for every problem space-or-type related
     * @throws InvalidOperationException whenever the received resource is not present in the marketBuffer first.
     * @param res   the resource to try to insert into the shelf (the same resource MUST be present in the market buffer before)
     * @param shelf the number of the shelf, starting from 1.
     * @return      true
     */
    @Override
    public boolean moveInShelf(PhysicalResource res, int shelf)
            throws ShelfInsertException, InvalidOperationException
    {
        shelf--;
        //first of all, check for "res" presence in the marketBuffer.
        long numbufferel = getBuffer().stream().filter((t)->t.getType().equals(res.getType())).count();
        if(res.getQuantity()>numbufferel)
            throw new InvalidOperationException("The resource is not present in the marketBuffer! Operation failed.");

        //resource is unknown or already placed on another shelf.
        if(res.getType().equals(ResType.UNKNOWN) || (getNumberOf(res.getType())>0 && !getWarehouseDisposition().get(shelf).getType().equals(res.getType())))
            throw new ShelfInsertException ("Error in shelf insert procedure (resource already present somewhere)! Operation failed.");

        if((res.getQuantity()+shelves.get(shelf).getQuantity()) > (shelf+1) ||
                (!res.getType().equals(shelves.get(shelf).getType()) && shelves.get(shelf).getQuantity()>0))
            throw new ShelfInsertException ("Error in shelf insert procedure! Operation failed.");

        //Shelf update:
        try {
            PhysicalResource newshelf = new PhysicalResource(res.getType(), shelves.get(shelf).getQuantity() + res.getQuantity());
            shelves.set(shelf, newshelf);
        }
        catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); }

        //marketBuffer cleaning:
        return cleanMarketBuffer(res);

    }

    /**
     * Receives the resource to remove from the marketBuffer, and does it one by one.
     * @param res the resource to remove from the marketBuffer
     * @return    true
     */
    public boolean cleanMarketBuffer(PhysicalResource res)
    {
        for(int i=res.getQuantity(); i>0; i--)
            try { marketBuffer.remove(new PhysicalResource(res.getType(), 1));}
            catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); }
        return true;
    }

    /**
     * This method returns the shelves List, converted to a Map.
     * @return an HashMap version of the warehouse status.
     */
    @Override
    public Map<ResType, Integer> getWarehouse()
    {
        Map<ResType, Integer> whMap = new HashMap<>();
        for(PhysicalResource shelf : shelves)
            whMap.put(shelf.getType(), shelf.getQuantity());
        return whMap;
    }

    /**
     * This method returns the perfect disposition of the warehouse.
     * @return an ArrayList representing the resources disposition in the warehouse.
     */
    public List<PhysicalResource> getWarehouseDisposition() { return new ArrayList<>(shelves); }

    /**
     * The method checks for the availability of the space in both resources before switching shelves,
     * avoiding the Undo procedure, then switches resources between the two shelves.
     * @throws ShelfInsertException when the switch is impossible due to resources quantities.
     * @param shelf1 the first shelf
     * @param shelf2 the second shelf
     * @return       true
     */
    @Override
    public boolean switchShelf(int shelf1, int shelf2)  throws ShelfInsertException
    {
        shelf1--; shelf2--;

        if(shelf1 < 0 || shelf2 < 0 || shelves.get(shelf1).getQuantity()>(shelf2+1) || shelves.get(shelf2).getQuantity()>(shelf1+1))
            throw new ShelfInsertException("Not enough space on both shelves to switch! Operation Failed.");

        PhysicalResource bufferShelf = shelves.get(shelf1);
        shelves.set(shelf1, shelves.get(shelf2));
        shelves.set(shelf2, bufferShelf);
        return true;
    }

    /**
     * @return the market buffer.
     */
    @Override
    public List<PhysicalResource> getBuffer() { return marketBuffer; }

    /**
     * This method, eventually, splits the received resource in more single quantities and inserts them in the marketBuffer.
     * @param res the resource to insert in the buffer
     * @return    true
     */
    @Override
    public boolean marketDraw(PhysicalResource res)
    {
        int i=0;
        while(i<res.getQuantity())
        {
            i++;
            try { marketBuffer.add(new PhysicalResource(res.getType(), 1)); }
            catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); }
        }
        return true;
    }

    /**
     * This method clears marketBuffer and returns its previous size, for penalty faith points purposes.
     * @return the remaining size of the buffer
     */
    @Override
    public int discardRemains()
    {
        int remainingSize= marketBuffer.size();
        marketBuffer.clear();
        return remainingSize;
    }

    /**
     * This method returns the quantity of resources of the requested type.
     * @param  type the type to count.
     * @return the number of type occurrences.
     */
    @Override
    public int getNumberOf(ResType type)
    {
        int number=0;
        for(PhysicalResource r : getWarehouseDisposition())
            number=number+(r.getType().equals(type) ? r.getQuantity() : 0);
        return number;
    }

    /**
     * This method returns the size of the requested shelf.
     * @param shelf the shelf to analyze
     * @return      the shelf size
     */
    @Override
    public int getShelfSize(int shelf)
    {
        return shelf;
    }
}