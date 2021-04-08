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
    private List<PhysicalResource> shelves;
    private List<PhysicalResource> marketBuffer;

    //constructor initializes the three shelves with UNKNOWN type and zero quantity.
    public ConcreteWarehouse() throws NegativeQuantityException
    {
        shelves = new ArrayList<>();
        shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
        shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
        shelves.add(new PhysicalResource(ResType.UNKNOWN, 0));
        marketBuffer = new ArrayList<>();
    }

    /*
    "shelf" parameter goes from 1 to 3 for the shelf selection.
    Returns the PhysicalResource in the chosen quantity, if possible, and removes it from the shelf.
    TESTED
     */
    @Override
    public PhysicalResource take(int shelf, int numresources) throws NotEnoughResourcesException , NegativeQuantityException
    {
        shelf--;
        int available= shelves.get(shelf).getQuantity();
        if(numresources > available)
            throw new NotEnoughResourcesException("Not enough resources in shelf "+shelf+"!");
        PhysicalResource newshelf = new PhysicalResource(shelves.get(shelf).getType(), (available-numresources));
        shelves.set(shelf, newshelf);
        return new PhysicalResource(shelves.get(shelf).getType(), numresources);
    }


    /*
    Moves in the specified shelf, if there is enough space or an EMPTY (quantity=0) shelf with whatever previous ResType in it.
    Throws ShelfInsertException for every problem space-or-type related, and InvalidOperationException whenever the received
        resource is not present in the marketBuffer first.
    TESTED
     */
    @Override
    public boolean moveInShelf(PhysicalResource res, int shelf)
            throws ShelfInsertException, NegativeQuantityException, InvalidOperationException
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
        PhysicalResource newshelf = new PhysicalResource(res.getType(), shelves.get(shelf).getQuantity()+res.getQuantity());
        shelves.set(shelf, newshelf);

        //marketBuffer cleaning:
        return cleanMarketBuffer(res);

    }

    //receives the resource to remove from the marketBuffer, and does it one by one. TESTED
    public boolean cleanMarketBuffer(PhysicalResource res) throws NegativeQuantityException
    {
        for(int i=res.getQuantity(); i>0; i--)
            marketBuffer.remove(new PhysicalResource(res.getType(), 1));
        return true;
    }

    //Returns the shelves List, converted to a Map.
    @Override
    public Map<ResType, Integer> getWarehouse()
    {
        Map<ResType, Integer> whMap = new HashMap<>();
        for(PhysicalResource shelf : shelves)
            whMap.put(shelf.getType(), shelf.getQuantity());
        return whMap;
    }

    //returns the perfect disposition of the warehouse. TESTED
    public List<PhysicalResource> getWarehouseDisposition()
    {
        List<PhysicalResource> list = new ArrayList<>();
        list.addAll(shelves);
        return list;
    }

    /*
    First of all, there must be enough space for the switch.
    Then, the method switches the resources.
    TESTED
     */
    @Override
    public boolean switchShelf(int shelf1, int shelf2)  throws ShelfInsertException
    {
        shelf1--; shelf2--;

        if(shelf1 < 0 || shelf2<0 || shelves.get(shelf1).getQuantity()>(shelf2+1) || shelves.get(shelf2).getQuantity()>(shelf1+1))
            throw new ShelfInsertException("Not enough space on both shelves to switch! Operation Failed.");

        PhysicalResource buffershelf = shelves.get(shelf1);
        shelves.set(shelf1, shelves.get(shelf2));
        shelves.set(shelf2, buffershelf);

        return true;
    }

    @Override
    public List<PhysicalResource> getBuffer() { return marketBuffer; }

    //Eventually, splits the received resource in a more single quantities and inserts them in the marketBuffer.
    @Override
    public boolean marketDraw(PhysicalResource res) throws NegativeQuantityException
    {
        int i=0;
        while(i<res.getQuantity())
        {
            i++;
            marketBuffer.add(new PhysicalResource(res.getType(), 1));
        }
        return true;
    }

    //Clears marketBuffer and returns its previous size.
    @Override
    public int discardRemains()
    {
        int remainingSize= marketBuffer.size();
        marketBuffer.clear();
        return remainingSize;
    }

    //Returns the quantity of resources of the requested type. TESTED
    @Override
    public int getNumberOf(ResType type)
    {
        int number=0;
        for(PhysicalResource r : getWarehouseDisposition())
            number=number+(r.getType().equals(type) ? r.getQuantity() : 0);
        return number;
    }

    @Override
    public int getShelfSize(int shelf)
    {
        return shelf;
    }
}