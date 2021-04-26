package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.*;

public class Player implements Adder, Verificator
{
    private final String nickname;
    private boolean connected;
    private Match match;
    private List<LeaderCard> handLeaders;
    private PersonalBoard personalBoard;
    private Production tempProduction;
    private DevelopmentCard tempDevCard;

    /**
     * This constructor creates a new unassociated player, without giving it the match.
     * @param nickname is the nickname of the player
     */
    public Player(String nickname)
    {
        this.nickname = nickname;
        connected = true;
        match = null;
    }

    //ALL SETTERS:
    /**
     * This method sets the initial configuration of the player's leaders.
     * @param handLeaders is the Leaders list
     * @return false
     */
    public boolean setHandLeaders(List<LeaderCard> handLeaders)
    {
        if(this.handLeaders == null)
        {
            this.handLeaders = handLeaders;
            return true;
        }
        return false;
    }

    /**
     * This method sets the Match to the player.
     * @param match is the Match instantiation
     * @return true if the player had no match, else false.
     */
    public boolean setMatch(Match match)
    {
        if (this.match == null)
        {
            this.match = match;
            return true;
        }
        return false;
    }

    /**
     * This method sets the player's personal Board.
     * @param personalBoard is the personal Board object.
     * @return true if the player has a match, else false.
     */
    public boolean setPersonalBoard(PersonalBoard personalBoard)
    {
        if (this.personalBoard == null)
        {
            this.personalBoard = personalBoard;
            return true;
        }
        return false;
    }

    /**
     * This method sets the temporary dev card.
     * @return true if the tempdevcard was null before, else false.
     */
    public boolean setTempDevCard(DevelopmentCard tempDevCard){
        if(this.tempDevCard==null) {
            this.tempDevCard = tempDevCard;
            return true;
        }
        return false;
    }

    // ----- ALL GETTERS -----
    /** @return match */
    public Match getMatch() { return match; }
    /** @return nickname */
    public String getNickname() { return nickname; }
    /** @return the state of the player's connection */
    public boolean isConnected() { return connected; }
    /** @return the hand leaders list */
    public List<LeaderCard> getHandLeaders() { return handLeaders; }
    /** @return personal Board */
    public PersonalBoard getPersonalBoard() { return personalBoard; }
    /** @return the possible white marble conversions */
    public List<PhysicalResource> getWhiteMarbleConversions() { return getPersonalBoard().getWhiteMarbleConversions(); }
    /** @return the temporary dev card to insert */
    public DevelopmentCard getTempDevCard() { return tempDevCard; }


    // ----- "ADDER" INTERFACE METHODS -----
    /**
     * This method adds faith points to the faith path, looking for the match end.
     * @param quantity is the quantity of steps
     * @return         true
     * @throws MatchEndedException if this player reaches the end of his faith path
     */
    @Override
    public boolean addFaithPoints(int quantity) throws MatchEndedException {
        getPersonalBoard().getFaithPath().addFaithPoints(quantity, match);
        return true;
    }

    /**
     * This method adds resources to the strongbox.
     * @param resource is the resource to insert
     * @return the confirmation of the operation
     * @see it.polimi.ingsw.model.match.player.personalBoard.StrongBox
     */
    @Override
    public boolean addToStrongBox(PhysicalResource resource) { return personalBoard.getStrongBox().put(resource); }

    /**
     * This method adds resources to the latest warehouse version.
     * @param resource is the resource to insert
     * @return the confirmation of the operation
     * @see it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse
     */
    @Override
    public boolean addToWarehouse(PhysicalResource resource) {
        return personalBoard.getWarehouse().marketDraw(resource);
    }

    //"VERIFICATOR" INTERFACE METHODS:
    /**
     * This method verifies if the resource is present, as sum of the resources in the deposits.
     * @param physicalResource is the resource to look for
     * @return                 true if it's presente, else false
     */
    @Override
    public boolean verifyResources(PhysicalResource physicalResource)
    {
        int numInWarehouse;
        int numInStrongbox;
        numInWarehouse = personalBoard.getWarehouse().getNumberOf(physicalResource.getType());
        numInStrongbox = personalBoard.getStrongBox().getNumberOf(physicalResource.getType());

        if (numInWarehouse >= physicalResource.getQuantity())
            return true;

        else if (numInStrongbox >= physicalResource.getQuantity())
            return true;

        else return numInStrongbox + numInWarehouse >= physicalResource.getQuantity();
    }

    /**
     * This method calls DevCardSlots' isSatisfied function.
     * @param card is the card to check
     * @return true if ok, else false
     * @see it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots
     */
    @Override
    public boolean verifyCard(CardType card) {
        return personalBoard.getDevCardSlots().isSatisfied(card);
    }

    /**
     * This method verifies a card's placeability basing on its level.
     * @param cardLevel is the level to check
     * @return          true if ok, else false
     * @see it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots
     */
    @Override
    public boolean verifyPlaceability(int cardLevel)
    {
        return personalBoard.getDevCardSlots().isPlaceable(cardLevel);
    }

    //OTHER METHODS:
    /** * This method connects the player.
     * @return true */
    public boolean connect() {connected=true; return true;}
    /** * This method disconnects the player.
     * @return true */
    public boolean disconnect() {connected=false; return true;}

    /**
     * This method sets the in-hand leaders chosen by the player.
     * @throws InvalidOperationException if the chosen leaders are not present in the handLeader's list.
     * @param choseLeaders a list containing the chosen starting leaders
     * @return             true
     */
    public boolean leadersChoice(ArrayList<LeaderCard> choseLeaders) throws InvalidOperationException
    {
        for(LeaderCard lc : choseLeaders)
            if(!handLeaders.contains(lc))
                throw new InvalidOperationException ("A chosen leader is not available in your hand! Retry.");
        handLeaders = choseLeaders;
        return true;
    }

    /**
     * This method checks for the possibility to activate the leader,
     * then activates it passing through the card's methods.
     * @param leader the card to activate
     * @return true if ok, else false
     */
    public boolean activateLeader(LeaderCard leader)
    {
        if(leader.isActivable(this))
        {
            handLeaders.remove(leader);
            leader.activate(getPersonalBoard());
            return true;
        }
        return false;
    }

    public boolean isProducible(Production production)
    {
        if(production.isPlayable(this))
        {
            tempProduction = production;
            return true;
        }
        return false;
    }

    /**
     * This method discards the non-chosen leaders from handLeader.
     * @requires the leader card must be contained in the handLeader list.
     * @param leader the leader to discard
     * @return       true
     */
    public boolean discardUselessLeader(LeaderCard leader)
    {
        handLeaders.remove(leader);
        return true;
    }

    /**
     * this method does a market action and returns the number of white marbles.
     * @param row    if true, represents a 'row' draw, else a 'column' draw.
     * @param number the number of the row or column
     * @return the number of white marble in the selected row or column
     */
    public int marketDeal(boolean row, int number) throws NegativeQuantityException, MatchEndedException
    {
        int whiteMarbles=0;
        if(row)
            //parameters errors are handled here.
            try{ whiteMarbles = match.getMarket().selectRow(number, this);}
            catch ( InvalidOperationException e){ e.printStackTrace(); }
        else
            //parameters errors are handled here.
            try{ whiteMarbles = match.getMarket().selectColumn(number, this);}
            catch ( InvalidOperationException e){ e.printStackTrace(); }
        return whiteMarbles;
    }

    /**
     *This method takes the Temp DevelopmentCard and puts it in tempDevCard.
     * @param gridR the chosen row
     * @param gridC the chosen column
     * @return      true
     */
    public boolean drawDevelopmentCard(int gridR, int gridC) throws MatchEndedException
    {
        try { setTempDevCard(match.getCardGrid().take(gridR, gridC)); }
        catch (InvalidOperationException e) { e.printStackTrace(); }
        return true;
    }


    /**
     *This method takes the Temp DevelopmentCard from the tempDevCard object and puts it in the DevCardSlot.
     * @requires The player must have already payed the relative resources himself before.
     * @param slot  the slot in which the card will be inserted
     * @return      true
     */
    public boolean insertDevelopmentCard(int slot) throws InvalidCardRequestException, MatchEndedException
    {
            try { getPersonalBoard().getDevCardSlots().pushNewCard(slot, tempDevCard); }
            catch (InvalidOperationException e) { e.printStackTrace(); }
        return true;
    }

    /**
     * This method tries to insert the resource into the warehouse.
     * Catches here the exceptions relative to moveInShelf procedure.
     * @param resource is the resource to insert
     * @param shelf    is the chosen shelf
     * @return         true if operation ended successfully, else false
     */
    public boolean moveIntoWarehouse(PhysicalResource resource, int shelf)
    {
        boolean ret = false;
        try
        {
            ret = personalBoard.getWarehouse().moveInShelf(resource, shelf);
        }
        catch(ShelfInsertException e) {System.err.println("Error in shelf moving action: retry.");}
        catch (InvalidOperationException e)
        {System.err.println("Critical error: the resource is not present in the marketBuffer first.");
         System.exit(1);}
        return ret;
    }

    /**
     * This method tries to pay "res" taking it from the warehouse.
     * Handles InvalidCardRequestException and NotEnoughResourcesException.
     * @param shelf is the chosen shelf
     * @param res   is the resource to pay
     * @return      true
     * @throws InvalidCardRequestException relative to the problem about the incompatibility
     *                                     between the requested resource and the one in the shelf
     */
    public boolean payFromWarehouse(PhysicalResource res, int shelf) throws InvalidCardRequestException
    {
        Warehouse wh = personalBoard.getWarehouse();
        if(!res.getType().equals(wh.getWarehouseDisposition().get(shelf-1).getType()))
            throw new InvalidCardRequestException ("The resource 'res' is not present on the shelf! Operation failed.");
        //NotEnoughResourcesException handled here.
        try { wh.take(shelf, res.getQuantity()); }
        catch(NotEnoughResourcesException e){ e.printStackTrace(); }
        return true;
    }

    /**
     * This method tries to pay "res" taking it from the strongbox.
     * Handles NotEnoughResourcesException.
     * @param res   is the resource to pay
     * @return      true
     */
    public boolean payFromStrongbox(PhysicalResource res)
    {
        StrongBox sb = personalBoard.getStrongBox();
        //NotEnoughResourcesException handled here.
        try { sb.take(res); }
        catch(NotEnoughResourcesException e){ e.printStackTrace(); }
        return true;
    }

    /**
     * this method produces the actual production in tempProduction.
     * @return true
     * @throws MatchEndedException (propagated)
     */
    public boolean produce() throws MatchEndedException
    {
        tempProduction.produce(this);
        return true;
    }

    /**
     * this method returns the player's total amount of win points.
     * @return int totalWinPoints
     */
    public int totalWinPoints()
    {
        int points = 0;
        PersonalBoard pb = getPersonalBoard();
        points += pb.getActiveLeaders().stream().map(LeaderCard::getWinPoints).reduce(points, Integer::sum);
        points += pb.getDevCardSlots().getWinPoints();
        points += pb.getFaithPath().getWinPoints();
        int partialPoints = 0;
        for(int value : pb.getWarehouse().getWarehouse().values())
            partialPoints += value;
        for(int value : pb.getStrongBox().getResources().values())
            partialPoints += value;
        points += partialPoints/5;

        return points;
    }

    @Override
    public String toString(){
        return nickname;
    }

}