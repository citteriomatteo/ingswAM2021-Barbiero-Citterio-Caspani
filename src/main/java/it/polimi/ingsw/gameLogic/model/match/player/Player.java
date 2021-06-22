package it.polimi.ingsw.gameLogic.model.match.player;

import it.polimi.ingsw.gameLogic.model.essentials.*;
import it.polimi.ingsw.gameLogic.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.SingleFaithPath;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.StrongBox;

import it.polimi.ingsw.gameLogic.model.observer.ModelObservable;

import java.util.*;

/**
 * This class implements the Player's identity.
 */
public class Player extends ModelObservable implements Adder, Verificator {
    private static final int WINNING_CONDITION_CARDS = 7;
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
    public Player(String nickname) {
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
    public boolean setInitialHandLeaders(List<LeaderCard> handLeaders) {
        this.handLeaders = handLeaders;
        return true;
    }

    /**
     * This method sets the initial configuration of the player's leaders.
     * @param handLeaders is the Leaders list
     * @return false
     */
    public boolean setHandLeaders(List<LeaderCard> handLeaders) {
        this.handLeaders = handLeaders;

        //update_call
        updateHandLeaders(this.nickname, handLeaders);

        return true;
    }

    /**
     * This method sets the Match to the player.
     * @param match is the Match instantiation
     * @return true if the player had no match, else false.
     */
    public boolean setMatch(Match match) {
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
    public boolean setPersonalBoard(PersonalBoard personalBoard) {
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

            //update_call
            //updateTempDevCard(this.nickname, tempDevCard);
            return true;
        }
        return false;
    }

    /**
     * Sets the tempProduction to the production that will be produce after the payments
     * @param tempProduction the production to be added to tempProduction
     */
    public void setTempProduction(Production tempProduction) {
        this.tempProduction = tempProduction;

        //update_call
        updateTempProduction(this.nickname, tempProduction);

    }

    // ----- ALL GETTERS -----
    /** @return match */
    public Match getMatch() { return match; }
    /** @return nickname */
    public String getNickname() { return nickname; }
    /** @return the state of the player's connection */
    public synchronized boolean isConnected() { return connected; }
    /** @return the hand leaders list */
    public List<LeaderCard> getHandLeaders() { return handLeaders; }
    /** @return personal Board */
    public PersonalBoard getPersonalBoard() { return personalBoard; }

    /** @return the possible white marble conversions */
    public List<PhysicalResource> getWhiteMarbleConversions() { return getPersonalBoard().getWhiteMarbleConversions(); }
    /** @return the temporary dev card to insert */
    public DevelopmentCard getTempDevCard() { return tempDevCard; }
    /**@return the temporary production to produce */
    public Production getTempProduction(){return tempProduction;}


    // ----- "ADDER" INTERFACE METHODS -----
    /**
     * This method adds faith points to the faith path, looking for the match end.
     * @param quantity is the quantity of steps
     * @return         true
     * @throws LastRoundException if this player reaches the end of his faith path
     */
    @Override
    public boolean addFaithPoints(int quantity) throws LastRoundException {
        try {
            getPersonalBoard().getFaithPath().addFaithPoints(quantity, match);
        }
        catch(LastRoundException e){
            //update_call
            updateFaithMarker(this.nickname, getPersonalBoard().getFaithPath().getPosition());
            throw e;
        }

        //update_call
        updateFaithMarker(this.nickname, getPersonalBoard().getFaithPath().getPosition());

        return true;
    }

    /**
     * This method adds points to the blackMarker (in single player).
     * @param quantity the quantity of faith points to Lorenzo
     * @return a boolean
     * @throws LastRoundException when Lorenzo reaches the end of the path
     */
    public boolean addBlackPoints(int quantity) throws LastRoundException {
        try{
            ((SingleFaithPath) getPersonalBoard().getFaithPath()).addBlackPoints(quantity, this);
        }
        catch(LastRoundException e){
            //update_call
            updateFaithMarker(this.nickname, getPersonalBoard().getFaithPath().getPosition());
            throw e;
        }

        //update_call
        updateLorenzoMarker(( (SingleFaithPath) this.getPersonalBoard().getFaithPath()).getBlackPosition());

        return true;
    }

    /**
     * This method calls the same method inside the warehouse.
     * Notifies every other player about the discarded resources (for faith points).
     * @return true
     * @throws InvalidOperationException if the discard operation is not valid.
     * @throws LastRoundException if discarding someone reaches the end of the path.
     */
    public boolean discardRemains() throws InvalidOperationException, LastRoundException {
        int remaining;
        try {
            remaining = getPersonalBoard().getWarehouse().discardRemains();
        }catch (InvalidOperationException e){
            //update_call
            this.updateWarehouse(nickname, personalBoard.getWarehouse());
            this.updateMarketBuffer(nickname, personalBoard.getWarehouse());
            throw e;
        }

        if(remaining>0) {
            try {
                for (Player p : match.getPlayers())
                    if (!p.equals(this))
                        p.addFaithPoints(remaining);

                //ADDED: if match is single, adds points also to Lorenzo
                if (match.getPlayers().size() == 1)
                    addBlackPoints(remaining);
            }
            catch(LastRoundException e){
                //update_call
                this.updateWarehouse(nickname, personalBoard.getWarehouse());
                this.updateMarketBuffer(nickname, personalBoard.getWarehouse());
                throw e;
            }
        }

        //update_call
        this.updateWarehouse(nickname, personalBoard.getWarehouse());
        this.updateMarketBuffer(nickname, personalBoard.getWarehouse());

        return true;
    }

    /**
     * This method adds resources to the strongbox.
     * @param resource is the resource to insert
     * @return the confirmation of the operation
     * @see StrongBox
     */
    @Override
    public boolean addToStrongBox(PhysicalResource resource) {
        return personalBoard.getStrongBox().put(resource);

    }

    /**
     * This method adds resources to the market buffer of the warehouse.
     * In order to put the resources inside the warehouse call {@link Player#moveIntoWarehouse(PhysicalResource, int)}
     * @param resource is the resource to insert
     * @return the confirmation of the operation
     * @see Warehouse
     */
    @Override
    public boolean addToWarehouse(PhysicalResource resource) {
        boolean res = personalBoard.getWarehouse().marketDraw(resource);
        updateMarketBuffer(nickname, getPersonalBoard().getWarehouse());
        return res;
    }

    //"VERIFICATOR" INTERFACE METHODS:
    /**
     * This method verifies if the resource is present, as sum of the resources in the deposits.
     * @param physicalResource is the resource to look for
     * @return                 true if it's present, else false
     */
    @Override
    public boolean verifyResources(PhysicalResource physicalResource) {
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
     * @see DevCardSlots
     */
    @Override
    public boolean verifyCard(CardType card) {
        return personalBoard.getDevCardSlots().isSatisfied(card);
    }

    /**
     * This method verifies a card's placeability basing on its level.
     * @param cardLevel is the level to check
     * @return          true if ok, else false
     * @see DevCardSlots
     */
    @Override
    public boolean verifyPlaceability(int cardLevel)
    {
        return personalBoard.getDevCardSlots().isPlaceable(cardLevel);
    }

    /**
     * This method gets some costs and discounts them.
     * @param initialCosts the costs to discount
     * @return the discounted costs list
     */
    @Override
    public List<PhysicalResource> getDiscountedCosts(List<PhysicalResource> initialCosts){
        Map<ResType, Integer> discountMap = personalBoard.getDiscountMap().getDiscountMap();
        List<PhysicalResource> discountedCosts = new ArrayList<>(initialCosts);

        for (ResType discount : discountMap.keySet()) {
            int index = discountedCosts.indexOf(new PhysicalResource(discount, discountMap.get(discount)));
            if(index != -1) {
                if (discountedCosts.get(index).getQuantity() > discountMap.get(discount)) {
                    PhysicalResource element = discountedCosts.remove(index);
                    discountedCosts.add(new PhysicalResource(discount, element.getQuantity() - discountMap.get(discount)));
                }
                else {
                    discountedCosts.remove(index);
                }
            }

        }

        return discountedCosts;
    }

    //OTHER METHODS:
    /**
     * Sets this player as connected, if this player was previously connected return false
     * @return true if this player wasn't already connected*/
    public synchronized boolean connect() {
        if(connected)
            return false;
        connected=true;
        return true;
    }

    /**
     * Sets this player as disconnected, if this player is already disconnected return false
     * @return true if this player wasn't already disconnected */
    public synchronized boolean disconnect() {
        if(!connected)
            return false;
        connected = false;
        return true;
    }

    /**
     * This method sets the in-hand leaders chosen by the player.
     * @throws InvalidOperationException if the chosen leaders are not present in the handLeader's list.
     * @param choseLeaders a list containing the chosen starting leaders
     * @return             true
     */
    public boolean leadersChoice(ArrayList<LeaderCard> choseLeaders) throws InvalidOperationException {
        for(LeaderCard lc : choseLeaders)
            if(!handLeaders.contains(lc))
                throw new InvalidOperationException ("A chosen leader is not available in your hand! Retry.");
        handLeaders = choseLeaders;

        //no update_call here!!!! at the start of turns, every player will be notified with a SummaryMessage.
        //updateHandLeaders(this.nickname, getHandLeaders());

        return true;
    }

    /**
     * This method checks for the possibility to activate the leader,
     * then activates it passing through the card's methods.
     * @param leader the card to activate
     * @return true if ok, else false
     */
    public boolean activateLeader(LeaderCard leader) {
        if(leader.isActivable(this))
        {
            //update_call
            updateActiveLeaders(this.nickname, leader);

            handLeaders.remove(leader);
            leader.activate(getPersonalBoard());

            //update_call
            updateHandLeaders(this.nickname, handLeaders);

            return true;
        }
        return false;
    }

    /**
     * This method tells whether the passed production is producible or not.
     * @param production the production
     * @return the result
     */
    public boolean isProducible(Production production) {
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
    public boolean discardUselessLeader(LeaderCard leader) throws LastRoundException {
        handLeaders.remove(leader);

        //update_call
        updateHandLeadersDiscard(this.nickname, leader);

        addFaithPoints(1);

        return true;
    }

    /**
     * This method calls other switch methods directly on the warehouse instance.
     * @param shelf1 first index
     * @param shelf2 second index
     * @return a boolean
     * @throws InvalidOperationException when the switch is impossible
     */
    public boolean switchShelf(int shelf1, int shelf2) throws InvalidOperationException {
        getPersonalBoard().getWarehouse().switchShelf(shelf1, shelf2);

        //update_call
        updateWarehouse(nickname, getPersonalBoard().getWarehouse());

        return true;
    }

    /**
     * this method does a market action and returns the number of white marbles.
     * @param row    if true, represents a 'row' draw, else a 'column' draw.
     * @param number the number of the row or column
     * @return the number of white marble in the selected row or column
     */
    public int marketDeal(boolean row, int number) throws InvalidOperationException, LastRoundException {
        int whiteMarbles;
        try {
            if (row)
                whiteMarbles = match.getMarket().selectRow(number, this);
            else
                //parameters errors are handled here.
                whiteMarbles = match.getMarket().selectColumn(number, this);
        }
        catch(LastRoundException e){
            //update_call
            updateMarketBuffer(this.nickname, getPersonalBoard().getWarehouse());
            updateMarket(getMatch().getMarket());
            throw e;
        }

        //update_call
        updateMarketBuffer(this.nickname, getPersonalBoard().getWarehouse());
        updateMarket(getMatch().getMarket());

        return whiteMarbles;
    }

    /**
     * This method inserts white conversions on market buffer.
     * @param numWhite the number of conversions
     * @return a boolean
     */
    public boolean whiteMarbleInsertion(int numWhite){
        if (getWhiteMarbleConversions().size() != 1)
            return false;
        PhysicalResource conversion = getWhiteMarbleConversions().get(0);
        for (int i=0; i<numWhite; i++)
            addToWarehouse(conversion);
        return true;
    }

    /**
     *This method takes the Temp DevelopmentCard and puts it in tempDevCard.
     * @param gridR the chosen row
     * @param gridC the chosen column
     * @return      true
     */
    public boolean drawDevelopmentCard(int gridR, int gridC) throws NoMoreCardsException, InvalidCardRequestException, LastRoundException {
        try {
            setTempDevCard(match.getCardGrid().take(gridR, gridC));
        }
        catch(LastRoundException e){
            //update_call
            updateTempDevCard(this.nickname, this.tempDevCard);
            updateCardGrid(getMatch().getCardGrid());
            throw e;
        }

        //update_call
        updateTempDevCard(this.nickname, this.tempDevCard);
        updateCardGrid(getMatch().getCardGrid());

        return true;
    }


    /**
     *This method takes the Temp DevelopmentCard from the tempDevCard object and puts it in the DevCardSlot.
     * @requires The player must have already payed the relative resources himself before.
     * @param slot  the slot in which the card will be inserted
     * @return      true
     */
    public boolean insertDevelopmentCard(int slot) throws LastRoundException, InvalidOperationException {
        personalBoard.getDevCardSlots().pushNewCard(slot, tempDevCard);
        tempDevCard = null;

        //update_call
        updateDevCardSlots(nickname, getPersonalBoard().getDevCardSlots());
        updateTempDevCard(nickname, tempDevCard);

        if(personalBoard.getDevCardSlots().getCardsNumber()>= WINNING_CONDITION_CARDS)
            throw new LastRoundException("The player " + this +
                    " has reached " + WINNING_CONDITION_CARDS + " cards in his DevCardSlots");

        return true;
    }

    /**
     * This method tries to insert the resource into the warehouse.
     * Catches here the exceptions relative to moveInShelf procedure.
     * It does not notify the player in order to avoid messages flooding.
     * @param resource is the resource to insert
     * @param shelf    is the chosen shelf
     * @return         true if operation ended successfully, else false
     */
    public boolean moveIntoWarehouse(PhysicalResource resource, int shelf) throws ShelfInsertException, InvalidQuantityException {
        return personalBoard.getWarehouse().moveInShelf(resource, shelf);
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
    public boolean payFromWarehouse(PhysicalResource res, int shelf) throws InvalidCardRequestException, NotEnoughResourcesException {
        Warehouse wh = personalBoard.getWarehouse();
        if(!res.getType().equals(wh.getWarehouseDisposition().get(shelf-1).getType())) {
            System.out.println("current warehouse: ");
            for(PhysicalResource r : wh.getWarehouseDisposition())
                System.out.println(r);
            throw new InvalidCardRequestException("The resource" + res + "is not present on the shelf! Operation failed.");
        }
        wh.take(shelf, res.getQuantity());

        return true;
    }

    /**
     * This method tries to pay "res" taking it from the strongbox.
     * Handles NotEnoughResourcesException.
     * @param res   is the resource to pay
     * @return      true
     */
    public boolean payFromStrongbox(PhysicalResource res) throws NotEnoughResourcesException {
        personalBoard.getStrongBox().take(res);
        return true;
    }

    /**
     * this method produces the actual production in tempProduction.
     * @return true
     * @throws LastRoundException if producing the tempProduction this player reaches the end of his FaithPath
     */
    public boolean produce() throws LastRoundException {
        try {
            tempProduction.produce(this);
        }
        catch(LastRoundException e){
            //update_call
            updateTempProduction(this.nickname, getTempProduction());
            throw e;
        }

        //update_call
        updateTempProduction(this.nickname, getTempProduction());

        return true;
    }

    /**
     * this method returns the player's total amount of win points.
     * @return int totalWinPoints
     */
    public int totalWinPoints() {
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

    /**
     * This method clears player's situation for a new match.
     * Used by the RematchPhaseController, in case of rematch.
     */
    public void clear(){
        connected = true;
        match = null;
        personalBoard = null;
        handLeaders = null;
        tempProduction = null;
        tempDevCard = null;
    }

    @Override
    public String toString(){
        return nickname;
    }
}