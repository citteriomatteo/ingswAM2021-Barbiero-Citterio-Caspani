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
    /**
     * This method uses getWarehouseDisposition() to check the possibility of the move,
     * then eventually does the job or delegates it to the lower shelves.
     * @throws NotEnoughResourcesException to check for resource availability
     * @param shelf        indicates the chosen shelf
     * @param numResources stays for the quantity of resources to take
     * @return             the requested resource
     * @see ConcreteWarehouse
     */
    PhysicalResource take(int shelf, int numResources) throws NotEnoughResourcesException;

    /**
     * This method redefines the simple moveInShelf and extends it to the new slots.
     * It also checks if the resource is of the same type of the leader shelf's one, even when quantity=0.
     * @throws ShelfInsertException      for invalid "shelf" values or not compatible resources move attempts.
     * @throws InvalidOperationException when the resource is not present of the market buffer.
     * @param res                        defines the resource to move
     * @param shelf                      indicates the chosen shelf
     * @see ConcreteWarehouse
     */
    boolean moveInShelf(PhysicalResource res, int shelf) throws ShelfInsertException, InvalidOperationException;

    /**
     * This method delegates the buffer clean operation to the concreteWarehouse.
     * @param res is the resource to clean
     * @return    true
     * @see ConcreteWarehouse
     */
    boolean cleanMarketBuffer(PhysicalResource res);

    /**
     * @return an HashMap version of the warehouse status, considering the 'decorating' shelves.
     * @see ConcreteWarehouse
     */
    Map<ResType, Integer> getWarehouse();

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
    boolean switchShelf(int shelf1, int shelf2) throws ShelfInsertException, InvalidOperationException;

    /**
     * This method clones the current Warehouse.
     * @param wh warehouse to clone
     * @return the cloned version of the warehouse
     */
     static Warehouse clone(Warehouse wh){
        Warehouse clonedWh = new ConcreteWarehouse();
        Warehouse.clone(wh, clonedWh);
        for(int i=3; i<wh.getWarehouseDisposition().size(); i++)
        {
            try{
                clonedWh = new ExtraShelfWarehouse(clonedWh, new PhysicalResource(wh.getWarehouseDisposition().get(i).getType(),wh.getShelfSize(i)));
            } catch(NegativeQuantityException e){ System.err.println("extra1: System shutdown due to an internal error."); System.exit(1); }
            try {
                clonedWh.marketDraw(wh.getWarehouseDisposition().get(i));
                clonedWh.moveInShelf(wh.getWarehouseDisposition().get(i), i+1);
            }
            catch(InvalidOperationException e){ System.err.println("extra2: System shutdown due to an internal error."); System.exit(1); }
        }
        return clonedWh;
    }
}