package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.FaithPath;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.ConcreteWarehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.ExtraShelfWarehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.*;

public class PersonalBoard implements Effecter
{
    private List<LeaderCard> activeLeaders, activeProductionLeaders;
    private List<PhysicalResource> whiteMarbleConversions;
    private Production basicProduction;
    private Warehouse warehouse;
    private StrongBox strongBox;
    private FaithPath faithPath;
    private DiscountMap discountMap;
    private DevCardSlots devCardSlots;

    public PersonalBoard(ArrayList<Cell> path, int startingPos, Production basicProduction) throws NegativeQuantityException
    {
        activeLeaders = new ArrayList<>();
        activeProductionLeaders = new ArrayList<>();
        whiteMarbleConversions = new ArrayList<>();
        this.basicProduction = basicProduction;
        warehouse = new ConcreteWarehouse();
        strongBox = new StrongBox();
        faithPath = new FaithPath(path, startingPos);
        discountMap = new DiscountMap();
        devCardSlots = new DevCardSlots();
    }

    /*
    Inserts the new leader into the activeLeaders list and,
    if it's a production one, also in the activeProductionLeaders list.
     */
    public boolean addActiveLeader(LeaderCard newleader, boolean hasProduction) throws NegativeQuantityException
    {
        activeLeaders.add(newleader);
        if(hasProduction)
            activeProductionLeaders.add(activeLeaders.get(activeLeaders.indexOf(newleader)));
        return true;
    }

    /*
    This method evolves the warehouse onto a new ExtraShelf version.
    The new shelf define procedure is made by the ExtraShelfWarehouse constructor.
     */
    public boolean warehouseEvolution(PhysicalResource extraShelf) throws NegativeQuantityException
    {
        Warehouse newWh = new ExtraShelfWarehouse(getWarehouse(), extraShelf);
        warehouse = newWh;
        return true;
    }

    public boolean addNewConversion(PhysicalResource conversion) { return getWhiteMarbleConversions().add(conversion); }

    //Inserts a new discount (the Resource defines it) in the discountMap.
    public boolean setDiscount(PhysicalResource discount)
    {
        return discountMap.setDiscount(discount);
    }

    //ALL GETTERS:
    public List<LeaderCard> getActiveLeaders() { return activeLeaders; }
    public List<LeaderCard> getActiveProductionLeaders() { return activeProductionLeaders; }
    public List<PhysicalResource> getWhiteMarbleConversions() { return whiteMarbleConversions; }
    public Production getBasicProduction() { return basicProduction; }
    public Warehouse getWarehouse() { return warehouse; }
    public StrongBox getStrongBox() { return strongBox; }
    public FaithPath getFaithPath() { return faithPath; }
    public Map<ResType, Integer> getDiscountMap() { return discountMap.getDiscountMap(); }
    public DevCardSlots getDevCardSlots() { return devCardSlots; }
}