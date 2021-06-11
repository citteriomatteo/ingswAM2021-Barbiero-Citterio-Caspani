package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.*;

import java.util.List;
import java.util.Map;

/**
 * This class implements the extra warehouse with an additional shelf.
 */
public class ExtraShelfWarehouse implements WarehouseDecorator
{
    private Warehouse oldWarehouse;
    private PhysicalResource extraShelf;
    private int shelfSize;

    /**
     * The constructor receives a PhysicalResource with quantity=shelfSize and puts it into extraShelfWarehouse splitting the quantity
     * in shelfSize and the PhysicalResource in extraShelf with quantity=0.
     * @param oldWarehouse is a reference to the last version of the warehouse (Concrete or already ExtraShelf)
     * @param extraShelf   contains a resource that will define the type and quantity of the extraShelf.
     * @see ConcreteWarehouse
     */
    public ExtraShelfWarehouse(Warehouse oldWarehouse, PhysicalResource extraShelf)
    {
        this.oldWarehouse = oldWarehouse;
        this.shelfSize = extraShelf.getQuantity();
        try { this.extraShelf = new PhysicalResource(extraShelf.getType(), 0); }
        catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+"."); }
    }

    // ----- ALREADY DEFINED METHODS IN THE OLDER VERSIONS OF THE WAREHOUSE -----
    /**
     * @return the marketBuffer
     * @see ConcreteWarehouse
     */
    @Override
    public List<PhysicalResource> getBuffer()
    {
        return oldWarehouse.getBuffer();
    }
    /**
     * @param res is the resource to insert into the marketBuffer, that is on the Concrete version of the warehouse.
     * @see ConcreteWarehouse
     */
    @Override
    public boolean marketDraw(PhysicalResource res) { return oldWarehouse.marketDraw(res); }

    /**
     * @return the quantity of resources remained.
     * @see ConcreteWarehouse
     */
    @Override
    public int discardRemains() throws InvalidOperationException
    {
        for(PhysicalResource r : getBuffer())
            if(extraShelf.getType().equals(r.getType()) && (extraShelf.getQuantity()+r.getQuantity())<=shelfSize)
                throw new InvalidOperationException("There's a space in warehouse for atleast a buffer resource! Retry to select.");

        return oldWarehouse.discardRemains();
    }

    /**
     * This method counts the requested resource on extraShelf and basic Shelves.
     * @param type the type to count.
     * @return     the quantity of the type.
     * @see ConcreteWarehouse
     */
    @Override
    public int getNumberOf(ResType type)
    {
        return (extraShelf.getType().equals(type) ? extraShelf.getQuantity() : 0) + oldWarehouse.getNumberOf(type);
    }

    // ----- METHODS TO REDEFINE -----
    /**
     * This method uses getWarehouseDisposition() to check the possibility of the move,
     * then eventually does the job or delegates it to the lower shelves.
     * @throws NotEnoughResourcesException to check for resource availability
     * @param shelf        indicates the chosen shelf
     * @param numResources stays for the quantity of resources to take
     * @return             the requested resource
     * @see ConcreteWarehouse
     */
    @Override
    public PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException
    {
        PhysicalResource takenRes=null;

        if(getWarehouseDisposition().size() == shelf)
        {
            if(numResources > extraShelf.getQuantity())
                throw new NotEnoughResourcesException("Not enough resources in the extra shelf!");
            try {
                extraShelf = new PhysicalResource(extraShelf.getType(), (extraShelf.getQuantity() - numResources));
                takenRes = new PhysicalResource(extraShelf.getType(), numResources);
            }
            catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+"."); }

        }
        else
            takenRes = oldWarehouse.take(shelf, numResources);

        return takenRes;
    }

    /**
     * This method redefines the simple moveInShelf and extends it to the new slots.
     * It also checks if the resource is of the same type of the leader shelf's one, even when quantity=0.
     * @throws ShelfInsertException      for invalid "shelf" values or not compatible resources move attempts.
     * @throws InvalidQuantityException when the resource is not present of the market buffer.
     * @param res                        defines the resource to move
     * @param shelf                      indicates the chosen shelf
     * @see ConcreteWarehouse
     */
    @Override
    public boolean moveInShelf(PhysicalResource res, int shelf) throws ShelfInsertException, InvalidQuantityException
    {
        if(shelf>getWarehouseDisposition().size())
            throw new ShelfInsertException ("Exception thrown: the shelf "+shelf+" does not exist.");

        if(getWarehouseDisposition().size() == shelf)
        {
            //first of all, check for "res" presence in the marketBuffer.
            long numBufferEl = getBuffer().stream().filter((t)->t.getType().equals(res.getType())).count();
            if(res.getQuantity()>numBufferEl)
                throw new InvalidQuantityException("The resource is not present in the marketBuffer! Operation failed.");

            if((res.getQuantity()+extraShelf.getQuantity())>shelfSize || !res.getType().equals(extraShelf.getType()))
                throw new ShelfInsertException("Error in leader shelf insert procedure! Operation cancelled.");

            //Shelf update:
            try
            {
                extraShelf = new PhysicalResource(extraShelf.getType(), extraShelf.getQuantity()+res.getQuantity());
            }
            catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+"."); }

            //bufferMarket cleaning:
            cleanMarketBuffer(res);
        }
        else
            oldWarehouse.moveInShelf(res, shelf);
        return true;
    }

    /**
     * This method delegates the buffer clean operation to the concreteWarehouse.
     * @param res is the resource to clean
     * @return    true
     * @see ConcreteWarehouse
     */
    @Override
    public boolean cleanMarketBuffer(PhysicalResource res)
    {
        return oldWarehouse.cleanMarketBuffer(res);
    }

    /**
     * @return an HashMap version of the warehouse status, considering the 'decorating' shelves.
     * @see ConcreteWarehouse
     */
    @Override
    public Map<ResType, Integer> getWarehouse()
    {
        Map<ResType, Integer> requestedMap = oldWarehouse.getWarehouse();
        requestedMap.merge(extraShelf.getType(), extraShelf.getQuantity(), Integer::sum);
       // requestedMap.replace(extraShelf.getType(), (requestedMap.get(extraShelf.getType()) == null ? 0 : requestedMap.get(extraShelf.getType())) + extraShelf.getQuantity());

        return requestedMap;
    }

    /**
     * @return a List representing all the extended warehouse status.
     */
    @Override
    public List<PhysicalResource> getWarehouseDisposition()
    {
        List<PhysicalResource> list = oldWarehouse.getWarehouseDisposition();
        list.add(extraShelf);
        return list;
    }

    /**
     * This method considers all the problems and dynamics involved
     * in switching resources between Extra and Basic shelves (such as type problems,
     * resources quantity problems), then eventually does the switch.
     * @throws ShelfInsertException      for switching quantities-related problems and issues between basic-extra/extra-extra types.
     * @throws InvalidOperationException for errors in parameters values.
     * @param shelf1                     stays for the first shelf
     * @param shelf2                     stays for the second shelf
     * @return                           true
     * @see ConcreteWarehouse
     */
    @Override
    public boolean switchShelf(int shelf1, int shelf2) throws ShelfInsertException, InvalidOperationException
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

            PhysicalResource buffRes1= take(shelf1, getWarehouseDisposition().get(shelf1-1).getQuantity());
            PhysicalResource buffRes2= take(shelf2, getWarehouseDisposition().get(shelf2-1).getQuantity());
            marketDraw(buffRes1); marketDraw(buffRes2);
            try {
                moveInShelf(buffRes2, shelf1);
                moveInShelf(buffRes1, shelf2);
            }
            catch(InvalidQuantityException | ShelfInsertException e){
                System.err.println("System shutdown due to an internal error."); System.exit(1);
            }
        }
        return true;
    }

    /**
     * This method returns the size of the specified shelf: calls old warehouse versions if the request has to drill down.
     * @param shelf indicates the chosen shelf
     * @return      the busy size of the shelf
     * @see ConcreteWarehouse
     */
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