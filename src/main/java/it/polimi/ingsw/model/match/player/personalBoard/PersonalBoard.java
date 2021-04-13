package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.FaithPath;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.MultiFaithPath;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.ConcreteWarehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.ExtraShelfWarehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.*;

public class PersonalBoard implements Effecter
{
    private final List<LeaderCard> activeLeaders;
    private final List<LeaderCard> activeProductionLeaders;
    private final List<PhysicalResource> whiteMarbleConversions;
    private final Production basicProduction;
    private Warehouse warehouse;
    private final StrongBox strongBox;
    private final FaithPath faithPath;
    private final DiscountMap discountMap;
    private final DevCardSlots devCardSlots;

    /**
     * This constructor implements the MultiFaithPath.
     * @param path            is the Cell-made path useful for the faithPath initialization
     * @param startingPos     indicates the starting point of the player on the path
     * @param basicProduction is the basic Production
     */

    public PersonalBoard(ArrayList<Cell> path, int startingPos, Production basicProduction) throws FaithPathCreationException
    {
        activeLeaders = new ArrayList<>();
        activeProductionLeaders = new ArrayList<>();
        whiteMarbleConversions = new ArrayList<>();
        this.basicProduction = basicProduction;
        warehouse = new ConcreteWarehouse();
        strongBox = new StrongBox();
        faithPath = new MultiFaithPath(path, startingPos);
        discountMap = new DiscountMap();
        devCardSlots = new DevCardSlots();
    }

    /**
     * This constructor implements the SingleFaithPath.
     * @param path            is the Cell-made path useful for the faithPath initialization
     * @param basicProduction is the basic Production
     */
    public PersonalBoard(ArrayList<Cell> path, Production basicProduction) throws FaithPathCreationException {
        activeLeaders = new ArrayList<>();
        activeProductionLeaders = new ArrayList<>();
        whiteMarbleConversions = new ArrayList<>();
        this.basicProduction = basicProduction;
        warehouse = new ConcreteWarehouse();
        strongBox = new StrongBox();
        faithPath = new SingleFaithPath(path, 0);
        discountMap = new DiscountMap();
        devCardSlots = new DevCardSlots();
    }

     /**
     * This method inserts the new leader into the activeLeaders list and,
     * if it's a Production one, also in the activeProductionLeaders list.
     * @param newLeader     is the new leader
     * @param hasProduction is a boolean identifying if it is a Production one or not
     * @return              true
     */
    public boolean addActiveLeader(LeaderCard newLeader, boolean hasProduction)
    {
        activeLeaders.add(newLeader);
        if(hasProduction)
            activeProductionLeaders.add(activeLeaders.get(activeLeaders.indexOf(newLeader)));
        return true;
    }

     /**
     * This method evolves the warehouse onto a new ExtraShelf version.
     * The new shelf define procedure is made by the ExtraShelfWarehouse constructor.
     * @param extraShelf is the resource that has all the informations about the new shelf (quantity and type)
     * @return           true
     */
    public boolean warehouseEvolution(PhysicalResource extraShelf)
    {
        warehouse = new ExtraShelfWarehouse(getWarehouse(), extraShelf);
        return true;
    }

    /**
     * This method inserts a new white-marble conversion in the whiteMarbleConversions list.
     * @param conversion is the new conversion to insert
     * @return           the operation on-finish flag
     */
    public boolean addNewConversion(PhysicalResource conversion) { return getWhiteMarbleConversions().add(conversion); }

    /**
     * This method inserts a new discount (the Resource defines it) in the discountMap.
     * @param discount defines the new discount
     * @return         true
     */
    public boolean setDiscount(PhysicalResource discount) { return discountMap.setDiscount(discount); }

    //ALL GETTERS:
    /** @return activeLeaders list */
    public List<LeaderCard> getActiveLeaders() { return activeLeaders; }
    /** @return activeProductionLeaders list */
    public List<LeaderCard> getActiveProductionLeaders() { return activeProductionLeaders; }
    /** @return whiteMarbleConversions list */
    public List<PhysicalResource> getWhiteMarbleConversions() { return whiteMarbleConversions; }
    /** @return the basic production */
    public Production getBasicProduction() { return basicProduction; }
    /** @return the warehouse */
    public Warehouse getWarehouse() { return warehouse; }
    /** @return the warehouse */
    public StrongBox getStrongBox() { return strongBox; }
    /** @return the strongbox */
    public FaithPath getFaithPath()
    {
        return faithPath;
    }
    /** @return the discount map */
    public Map<ResType, Integer> getDiscountMap() { return discountMap.getDiscountMap(); }
    /** @return the Development cards slots */
    public DevCardSlots getDevCardSlots() { return devCardSlots; }
}