package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.exceptions.ShelfInsertException;

import java.util.List;
import java.util.Map;

public class ExtraShelfWarehouse implements WarehouseDecorator
{
    private Warehouse oldWarehouse;
    private PhysicalResource extraShelf;
    private int shelfSize;

    /*
    to check!! -> receives a PhysicalResource with quantity=shelfSize and puts it into extraShelfWarehouse splitting the quantity
                    in shelfSize and the PhysicalResource in extraShelf with quantity=0.
     */
    public ExtraShelfWarehouse(Warehouse oldWarehouse, PhysicalResource extraShelf) throws NegativeQuantityException
    {
        this.oldWarehouse = oldWarehouse;
        this.shelfSize = extraShelf.getQuantity();
        this.extraShelf = new PhysicalResource(extraShelf.getType(), 0);
    }

    //ALREADY DEFINED METHODS IN THE OLDER VERSIONS OF THE WAREHOUSE:

    @Override
    public List<PhysicalResource> getBuffer()
    {
        return oldWarehouse.getBuffer();
    }

    @Override
    public boolean marketDraw(PhysicalResource res) throws NegativeQuantityException { return oldWarehouse.marketDraw(res); }

    @Override
    public int discardRemains() {
        return oldWarehouse.discardRemains();
    }

    @Override
    public int getNumberOf(ResType type) {
        return (extraShelf.getType().equals(type) ? extraShelf.getQuantity() : 0) + oldWarehouse.getNumberOf(type);
    }

    //METHODS TO REDEFINE:

    //Uses getWarehouseDisposition() to check the possibility of the move, then eventually does the job or delegates it. TESTED
    @Override
    public PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException, NegativeQuantityException
    {
        PhysicalResource takenres;

        if(getWarehouseDisposition().size() == shelf)
        {
            if(numResources > extraShelf.getQuantity())
                throw new NotEnoughResourcesException("Not enough resources in the extra shelf!");
            extraShelf = new PhysicalResource(extraShelf.getType(), (extraShelf.getQuantity()-numResources));
            takenres = new PhysicalResource(extraShelf.getType(), numResources);
        }
        else
            takenres = oldWarehouse.take(shelf, numResources);

        return takenres;
    }

    //Redefines the simple moveInShelf and extends it to the new slots. TESTED
    @Override
    public boolean moveInShelf(PhysicalResource res, int shelf) throws ShelfInsertException, NegativeQuantityException, InvalidOperationException
    {
        if(shelf>getWarehouseDisposition().size())
            throw new ShelfInsertException ("Exception thrown: the shelf "+shelf+" does not exist.");

        if(getWarehouseDisposition().size() == shelf)
        {
            //first of all, check for "res" presence in the marketBuffer.
            long numbufferel = getBuffer().stream().filter((t)->t.getType().equals(res.getType())).count();
            if(res.getQuantity()>numbufferel)
                throw new InvalidOperationException("The resource is not present in the marketBuffer! Operation failed.");

            if((res.getQuantity()+extraShelf.getQuantity())>shelfSize || !res.getType().equals(extraShelf.getType()))
                throw new ShelfInsertException("Error in leader shelf insert procedure! Operation cancelled.");

            //Shelf update:
            extraShelf = new PhysicalResource(extraShelf.getType(), extraShelf.getQuantity()+res.getQuantity());

            //bufferMarket cleaning:
            cleanMarketBuffer(res);
        }
        else
            oldWarehouse.moveInShelf(res, shelf);
        return true;
    }

    //TESTED (instructions covered)
    @Override
    public boolean cleanMarketBuffer(PhysicalResource res) throws NegativeQuantityException
    {
        return oldWarehouse.cleanMarketBuffer(res);
    }

    @Override
    public Map<ResType, Integer> getWarehouse()
    {
        Map<ResType, Integer> requestedMap = oldWarehouse.getWarehouse();
        requestedMap.replace(extraShelf.getType(), (requestedMap.get(extraShelf.getType()) + extraShelf.getQuantity()));
        return requestedMap;
    }

    //TESTED (used in CorrectDisposition.... -> instructions covered)
    @Override
    public List<PhysicalResource> getWarehouseDisposition()
    {
        List<PhysicalResource> list = oldWarehouse.getWarehouseDisposition();
        list.add(extraShelf);
        return list;
    }

    /*
    This method considers all the problems and dynamics involved
        in switching resources between extra and basic shelves, then does it.

    TESTED
     */
    @Override
    public boolean switchShelf(int shelf1, int shelf2)
            throws ShelfInsertException, NegativeQuantityException, InvalidOperationException, NotEnoughResourcesException
    {
        if(shelf1<0 || shelf1>getWarehouseDisposition().size() || shelf2<0 || shelf2>getWarehouseDisposition().size())
            throw new InvalidOperationException ("Incorrect attributes! Operation Failed.");
        if(shelf1<=3 && shelf2<=3)
            oldWarehouse.switchShelf(shelf1, shelf2);
        else
        {
            List<PhysicalResource> disp = getWarehouseDisposition();
            if(disp.get(shelf1-1).getQuantity()>getShelfSize(shelf2) || disp.get(shelf2-1).getQuantity()>getShelfSize(shelf1))
                throw new ShelfInsertException("Not enough space on both shelves to switch! Operation Failed.");

            if(shelf1>3 && shelf2<=3)
            {
                int buff = shelf2;
                shelf2 = shelf1;
                shelf1 = buff;
            }
            if(shelf1<=3)
                for(int i=0; i<3; i++)
                    if(i!=(shelf1-1) && disp.get(i).getType().equals(disp.get(shelf2-1).getType()))
                        throw new ShelfInsertException ("Impossible switch: resource type"+disp.get(shelf2-1).getType()+" is already present on basic warehouse.");
            else
                if(!disp.get(shelf1-1).getType().equals(disp.get(shelf2-1).getType()))
                    throw new ShelfInsertException("Impossible switch: two leaders slots are not compatible.");

            PhysicalResource buffres1= take(shelf1, getWarehouseDisposition().get(shelf1-1).getQuantity());
            PhysicalResource buffres2= take(shelf2, getWarehouseDisposition().get(shelf2-1).getQuantity());
            marketDraw(buffres1); marketDraw(buffres2);
            moveInShelf(buffres2,shelf1);
            moveInShelf(buffres1, shelf2);
        }
        return true;
    }

    //Returns the size of the specified shelf: calls old warehouse versions if the request has to drill down.
    @Override
    public int getShelfSize(int shelf)
    {
        int size;
        if(shelf<getWarehouseDisposition().size())
            size=oldWarehouse.getShelfSize(shelf);
        else
            size=shelfSize;
        return size;
    }
}