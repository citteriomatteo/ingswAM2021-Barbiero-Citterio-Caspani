package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.ProductionEffect;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.network.message.stocmessage.LastRoundMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;

import static it.polimi.ingsw.controller.MatchController.getKeyByValue;


import java.util.*;
import java.util.stream.Collectors;

/**
 * This controller handles all the messages and moves that characterize the turn phase.
 */

public class TurnController {
    private Player currentPlayer;
    private boolean lastRound;
    private StateName currentState;
    private final Match match;
    private final Map<String, Card> cardMap;
    private int whiteMarbleDrawn;
    private String bufferResult="";


    /**
     * The constructor initializes every player in waiting for turn, except for the first.
     * @param match the match
     * @param cardMap the cards map to save
     */
    public TurnController(Match match, Map<String, Card> cardMap) {
        this.lastRound = false;
        this.currentPlayer = match.getCurrentPlayer();
        this.currentState = StateName.STARTING_TURN;
        for(Player p : match.getPlayers())
            if(!p.equals(currentPlayer))
                p.updateLastUsedState(p.getNickname(), StateName.WAITING_FOR_TURN);
            else
                p.updateLastUsedState(p.getNickname(), currentState);

        this.match = match;
        this.cardMap = cardMap;
        this.whiteMarbleDrawn = 0;

        //sends NextStateMessage to every player
        for(Player p : match.getPlayers())
            new NextStateMessage(p.getNickname(), p.getSummary().getPlayerSummary(p.getNickname()).getLastUsedState()).send(p.getNickname());

    }

    /**
     * This method goes on with the turn, return the new state of the player and eventually notifying the end of match.
     * @return a StateName
     */
    public StateName nextTurn() throws MatchEndedException {
        //If it's the last turn of the last player of the last round...
        if(currentPlayer.equals(match.getPlayers().get(match.getPlayers().size()-1)) && currentState.equals(StateName.END_TURN) && lastRound){
            Map<String, Integer> ranking = new HashMap<>();
            for(Player p : match.getPlayers())
                ranking.put(p.getNickname(), p.totalWinPoints());
            throw new MatchEndedException(bufferResult+"\nMatch Ranking:", ranking);
        }

        else if(currentState.equals(StateName.END_TURN) || !currentPlayer.isConnected()) {
                try{
                    currentState = match.nextTurn();
                }
                catch(LastRoundException e){ //Enter here only for singlePlayer
                    Map<String, Integer> myScore = new HashMap<>();
                    myScore.put(currentPlayer.getNickname(), currentPlayer.totalWinPoints());
                    throw new MatchEndedException("You lost!", myScore);
                }
                currentPlayer = match.getCurrentPlayer();
        }
        return currentState;
    }


    //METHODS FOR THE SINGLE EVENT HANDLING:

    /**
     * This method does the switch between two shelves, if possible.
     * @param shelf1 the first shelf
     * @param shelf2 the second shelf
     * @return the new State of the controller and client
     * @throws RetryException when the parameters are wrong
     */
    public StateName switchShelf(int shelf1, int shelf2) throws RetryException {
        try {
            currentPlayer.switchShelf(shelf1, shelf2);
        } catch (InvalidOperationException e) {
            throw new RetryException ("Invalid shelves switch.");
        }
        return currentState;
    }

    /**
     * This method does the discard of the chosen leader.
     * @param leaderId is the id of the leader card
     * @return the new State of the controller and client
     * @throws RetryException when the activation is not possible
     */
    public StateName leaderActivation(String leaderId) throws RetryException {

        for(LeaderCard card : currentPlayer.getHandLeaders())
            if (card.equals(cardMap.get(leaderId))) {
                if(!currentPlayer.activateLeader(card))
                    break;
                return currentState;
            }
        throw new RetryException ("Invalid leader activation attempt.");
    }

    /**
     * This method does the activate of the chosen leader.
     * @param leaderId is the id of the leader card
     * @return the new State of the controller and client
     * @throws RetryException if the discard is not possible
     */
    public StateName leaderDiscarding(String leaderId) throws RetryException {
        for(LeaderCard card : currentPlayer.getHandLeaders())
            if (card.equals(cardMap.get(leaderId))) {
                try {
                    currentPlayer.discardUselessLeader(card);
                } catch (LastRoundException e) { isLastRound(); }
                return currentState;
            }
        throw new RetryException ("Invalid leader discarding attempt.");
    }

    /**
     * This method does the draw from the market and decides if
     * the controller will be waiting for white marble conversions or straight warehouse insert choices.
     * @param row the choice between row and column
     * @param num the value of the choice
     * @return the new State of the controller and client
     * @throws RetryException when the draw is not possible due to invalid parameters.
     */
    public StateName marketDraw(boolean row, int num) throws RetryException {
        try {
            whiteMarbleDrawn = currentPlayer.marketDeal(row, num);
            if(whiteMarbleDrawn == 0 || currentPlayer.getWhiteMarbleConversions().size() == 0) {
                if (currentPlayer.getPersonalBoard().getWarehouse().getBuffer().size() == 0)
                    changeState(StateName.END_TURN);
                else
                    changeState(StateName.RESOURCES_PLACEMENT);
            }
            else {
                if(currentPlayer.whiteMarbleInsertion(whiteMarbleDrawn))
                    changeState(StateName.RESOURCES_PLACEMENT);
                else
                    changeState(StateName.MARKET_ACTION);
            }

        } catch (LastRoundException e) { isLastRound(); }
        catch (InvalidOperationException e) {
            throw new RetryException (e.getMessage());
        }
        return currentState;
    }

    /**
     * This method checks if the chosen conversions are made available by the player's actual active leaders list.
     * Then it inserts the resources in the buffer.
     * @param resources the conversions
     * @return the new State of the controller and client
     * @throws RetryException when the conversion is not acceptable
     */
    public StateName whiteMarblesConversion(List<PhysicalResource> resources) throws RetryException {
        if(resources.size() != whiteMarbleDrawn) {
            throw new RetryException ("Invalid conversions.");
        }
        for(PhysicalResource sentConversion : resources) {
            boolean found=false;
            for(PhysicalResource possibleConversion : currentPlayer.getWhiteMarbleConversions())
                if(possibleConversion.equals(sentConversion) && !found) {
                    currentPlayer.addToWarehouse(sentConversion);
                    found=true;
                    whiteMarbleDrawn -= possibleConversion.getQuantity();
                }
            if(!found)
                throw new RetryException ("Invalid conversions.");
        }

        //update_call outside of the match to avoid multiple buffer updates
        currentPlayer.updateMarketBuffer(currentPlayer.getNickname(), currentPlayer.getPersonalBoard().getWarehouse());

        if(whiteMarbleDrawn==0)
            changeState(StateName.RESOURCES_PLACEMENT);
        else
            throw new RetryException("You can still convert "+ whiteMarbleDrawn +" white marbles.");

        return currentState;
    }

    /** This method checks if the resources are insertable in the chosen shelves.
     * - If something goes wrong, leaves the already inserted resources in the shelves and the controller expects a
     *   new Message with new shelf choices.
     * - If it works well, it proceeds with trying to discard resources in the buffer, if it's still not possible
     *   the controller will expect shelf choices again.
     * The process iterates until the buffer is discardable.
     * @param resources the resources list, with quantity -> shelf.
     * @return the new State of the controller and client
     * @throws RetryException when the insertion is not acceptable
     */
    public StateName warehouseInsertion(List<PhysicalResource> resources) throws RetryException {
        List<Boolean> errors = new ArrayList<>();
        StringBuilder errMessage = new StringBuilder();
        if(resources.size() == 1 && resources.get(0).getType().equals(ResType.UNKNOWN)) {
            try {
                currentPlayer.discardRemains();
                changeState(StateName.END_TURN);
                return currentState;
            }
            catch (InvalidOperationException e) {
                throw new RetryException ("You can still place other resources." + errMessage);
            }
            catch (LastRoundException e) {
                isLastRound();
                try {
                    currentPlayer.discardRemains();
                    changeState(StateName.END_TURN);
                    return currentState;
                } catch (InvalidOperationException invalidOperationException) {
                    throw new RetryException ("You can still place other resources." + errMessage);
                } catch (LastRoundException lastRoundException) {
                    changeState(StateName.END_TURN);
                    return currentState;
                }
            }
        }

        for(PhysicalResource resource : resources)
            errors.add(singleWarehouseMove(resource));
        if(errors.contains(false)){
            try {
                currentPlayer.discardRemains();
                changeState(StateName.END_TURN);
                return currentState;
            }
            catch (LastRoundException e) {
                isLastRound();
                try {
                    currentPlayer.discardRemains();
                    changeState(StateName.END_TURN);
                    return currentState;
                } catch (InvalidOperationException invalidOperationException) {
                    throw new RetryException ("You can still place other resources." + errMessage);
                } catch (LastRoundException lastRoundException) {
                    changeState(StateName.END_TURN);
                    return currentState;
                }
            }
            catch (InvalidOperationException e) {
                errMessage.append("You can still place other resources.\n");
            }
        }

        if(errors.contains(true)) {
            for (int i = 0; i < errors.size(); i++)
                if (errors.get(i))
                    errMessage.append("(invalid insert choice number ").append(i + 1).append(")");
        }

        throw new RetryException (errMessage.toString());
    }

    /**
     * This method, after having received a DevCardDrawMessage, checks first if the chosen card is placeable and buyable.
     * Then, he takes the card and puts it in the TempDevCard slot (not paid yet).
     * @param row    the row
     * @param column the column
     * @return       the new State of the controller and client
     * @throws RetryException if the chosen draw is not possible
     */
    public StateName devCardDraw(int row, int column) throws RetryException {
        try {
            if(!match.getCardGrid().isBuyable(currentPlayer,row, column)) {
                throw new RetryException ("You can't buy this card.");
            }
            else if(!currentPlayer.verifyPlaceability(row)) {
                throw new RetryException ("You can't place this card.");
            }
            else {
                currentPlayer.drawDevelopmentCard(row,column);
                changeState(StateName.BUY_DEV_ACTION);
            }
        } catch (InvalidCardRequestException e) {
            throw new RetryException ("Invalid development card draw parameters.");
        } catch (NoMoreCardsException e) {
            throw new RetryException ("The selected cell is empty.");
        }
        catch (LastRoundException e) {
            isLastRound();
            bufferResult = "Last card of the column has been drawn. You lost!";
        }

        return currentState;
    }

    /**
     * This method is used in production/devCard payments and
     *   clones the warehouse and strongbox states before payments and tries to pay everything.
     * If something goes wrong, it refreshes the old storage versions.
     * Does an additional control for the production payment, summing the resources by type for a simple
     *   no-order "equals" operation with the TempProduction and then, eventually, produces it.
     * @param strongboxCosts the list of resources for the Strongbox
     * @param warehouseCosts the map of resources to insert in the warehouse
     * @return               the new State of the controller and client
     * @throws RetryException when the payments are not valid
     */
    public StateName payments(List<PhysicalResource> strongboxCosts, Map<Integer, PhysicalResource> warehouseCosts) throws RetryException {
        StrongBox sbUndo = StrongBox.clone(currentPlayer.getPersonalBoard().getStrongBox());
        Warehouse whUndo = WarehouseDecorator.clone(currentPlayer.getPersonalBoard().getWarehouse());

        PhysicalResource voidResource = new PhysicalResource(ResType.UNKNOWN, 0);

        if(strongboxCosts == null || warehouseCosts == null)
            throw new RetryException("StrongboxCosts or WarehouseCosts are null");

        strongboxCosts.remove(voidResource);
        warehouseCosts.remove(0);

        List<PhysicalResource> payments = new ArrayList<>();
        payments.addAll(strongboxCosts);
        payments.addAll(warehouseCosts.values());

        int quantity;
        int i=0;
        boolean isDouble = false;
        while (i < payments.size()){
            for (int j = i+1; j < payments.size(); j++){
                if (payments.get(i).equals(payments.get(j))) {
                    PhysicalResource r = payments.get(i);
                    PhysicalResource r1 = payments.get(j);
                    quantity = r.getQuantity() + r1.getQuantity();
                    payments.remove(r);
                    payments.remove(r1);
                    payments.add(new PhysicalResource(r.getType(), quantity));
                    isDouble = true;
                }
            }
            if(!isDouble)
                i++;

            isDouble = false;
        }

        Map<ResType, Integer> discounts = currentPlayer.getPersonalBoard().getDiscountMap().getDiscountMap();

        if(currentState == StateName.PRODUCTION_ACTION){

            if(payments.size() != currentPlayer.getTempProduction().getCost().size())
                throw new RetryException("Payments don't match the chosen production ones.");
            boolean found = false;
            for (PhysicalResource p : payments) {
                for (PhysicalResource p1 : currentPlayer.getTempProduction().getCost())
                    if (p.equals(p1) && p.getQuantity() == p1.getQuantity()) {
                        found = true;
                        break;
                    }
                if(!found)
                    throw new RetryException("Payments don't match the chosen production ones.");
                found = false;
            }
        }
        else{
            List<PhysicalResource> actualCosts = new ArrayList<>(currentPlayer.getTempDevCard().getPrice());
            removeDiscounts(discounts, actualCosts);

            if(payments.size() != actualCosts.size())
                throw new RetryException("Payments don't match the chosen production ones.");
            boolean found = false;
            for (PhysicalResource p : payments) {
                for (PhysicalResource p1 : actualCosts)
                    if (p.equals(p1) && p.getQuantity() == p1.getQuantity()) {
                        found = true;
                        break;
                    }
                if(!found)
                    throw new RetryException("Payments don't match the chosen production ones.");
                found = false;
            }
        }

        for(PhysicalResource r : strongboxCosts)
            try {
                currentPlayer.payFromStrongbox(r);
            } catch (NotEnoughResourcesException e) {
                //Re-inserting resources in the Strongbox
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
                throw new RetryException (e.getMessage());
            }

        for(PhysicalResource r : warehouseCosts.values()) {
            try {
                currentPlayer.payFromWarehouse(r,getKeyByValue(warehouseCosts,r));
            } catch (InvalidOperationException e) {
                currentPlayer.getPersonalBoard().setWarehouse(whUndo);
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
                throw new RetryException (e.getMessage());
            }
        }

        if(currentState.getVal() == StateName.BUY_DEV_ACTION.getVal())
            changeState(StateName.PLACE_DEV_CARD);

        else if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()) {
            try {
                currentPlayer.getTempProduction().produce(currentPlayer);
            }
            catch (LastRoundException e) { isLastRound(); }
            changeState(StateName.END_TURN);
        }

        //global update_call at the end to avoid messages flooding
        currentPlayer.updateWarehouse(currentPlayer.getNickname(),currentPlayer.getPersonalBoard().getWarehouse());
        currentPlayer.updateStrongbox(currentPlayer.getNickname(),currentPlayer.getPersonalBoard().getStrongBox());

        return currentState;
    }

    /**
     * This method, after having received a DevCardPlacementMessage, tries to insert the card in TempDevCard and
     * updates sets the new state to END_TURN.
     * @param column    the chosen dev slot
     * @return the new State of the controller and client
     * @throws RetryException if the operation is invalid, somehow.
     */
    public StateName devCardPlacement(int column) throws RetryException {
        try {
            currentPlayer.insertDevelopmentCard(column);
        } catch (LastRoundException e) {
            isLastRound();
            if(match.getPlayers().size()==1)
                bufferResult = "You bought the 7th card. You won!";
        }
        catch (InvalidOperationException e) {
            throw new RetryException (e.getMessage());
        }
        changeState(StateName.END_TURN);
        return currentState;
    }

    /**
     * This method, after having received a ProductionMessage, checks:
     * - if the player has all the cards that he wants to produce;
     * - checks if the number of unknown costs and earnings corresponds to the chosen cards unknown quantities;
     * and aborts the operation in both cases, if something went wrong.
     * Then, this method correctly builds the temporary production and insert it into tempProduction (for next).
     * @param cardIds the list of the cards to produce
     * @param productionOfUnknown the summative production of all the unknowns
     * @return the new State of the controller and client
     * @throws RetryException when the production is not acceptable and the reasons may be: not enough resources in the production,
     *                         unknowns do not correspond to the effective payments, ...
     */
    public StateName production(List<String> cardIds, Production productionOfUnknown) throws RetryException {
        //checking if the player has all the cards that he wants to produce
        boolean basicProdFound=false;
        List<String> cardsFound = new ArrayList<>();
        List<Boolean> cardsErrors = new ArrayList<>();
        for(int i=0; i < cardIds.size(); i++)
            if(!cardIds.get(i).equals("BASICPROD")) {
                String id = cardIds.get(i);
                Card card = cardMap.get(id);
                if(!(currentPlayer.getPersonalBoard().getDevCardSlots().getTop().contains(card) ||
                        currentPlayer.getPersonalBoard().getActiveProductionLeaders().contains(card)) ||
                            cardsFound.contains(id)) {
                    cardsErrors.add(true);
                }
                else{
                    cardsErrors.add(false);
                    cardsFound.add(id);
                }
            }
            else {
                //adds an error if the basic prod was previously found otherwise adds a false
                cardsErrors.add(basicProdFound);
                basicProdFound = true;
            }

        if(cardsErrors.contains(true)) {
            StringBuilder errMessage = new StringBuilder("Invalid insert choices: ");
            for (int i = 0; i < cardsErrors.size(); i++)
                if (cardsErrors.get(i))
                    errMessage.append(i).append(" ");

            errMessage.append("\n");
            throw new RetryException (errMessage.toString());
        }

        //checks if the number of unknown costs and earnings corresponds to the chosen cards unknown quantities
        int uCosts=0, uEarnings=0;
        if(basicProdFound) {
            uCosts += currentPlayer.getPersonalBoard().getBasicProduction().getCost().stream().filter((x)->x.getType().equals(ResType.UNKNOWN)).map(PhysicalResource::getQuantity).reduce(0, Integer::sum);
            for(Resource r : currentPlayer.getPersonalBoard().getBasicProduction().getEarnings())
                if(r.isPhysical()){
                    PhysicalResource usefulR = (PhysicalResource) r;
                    uEarnings += (usefulR.getType().equals(ResType.UNKNOWN) ? usefulR.getQuantity() : 0);
                }
        }
        for(String id : cardsFound){
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            uCosts += prod.getCost().stream()
                    .filter((x)->x.getType().equals(ResType.UNKNOWN))
                    .map(PhysicalResource::getQuantity).reduce(0, Integer::sum);
            for(Resource r : prod.getEarnings())
                if(r.isPhysical()){
                    PhysicalResource usefulR = (PhysicalResource) r;
                    uEarnings += (usefulR.getType().equals(ResType.UNKNOWN) ? usefulR.getQuantity() : 0);
                }
        }

        int numCosts=0;
        for (PhysicalResource r : productionOfUnknown.getCost())
            numCosts+=r.getQuantity();
        int numEarnings=0;
        for (Resource r : productionOfUnknown.getEarnings())
            if(r.isPhysical()){
                PhysicalResource usefulR = (PhysicalResource) r;
                numEarnings += usefulR.getQuantity();
            }

        if(uCosts != numCosts || uEarnings != numEarnings) {
            throw new RetryException ("Invalid choices for unknown conversions.");
        }

        //create the tempProduction
        List<PhysicalResource> totalCosts = new ArrayList<>(productionOfUnknown.getCost());
        List<Resource> totalEarnings = new ArrayList<>(productionOfUnknown.getEarnings());

        for(String id : cardsFound) {
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            totalCosts.addAll(prod.getCost());
            totalEarnings.addAll(prod.getEarnings());
        }

        totalCosts = totalCosts.stream().filter((x)->(!x.getType().equals(ResType.UNKNOWN))).collect(Collectors.toList());

        for(int i=0; i<totalEarnings.size(); i++)
            if(totalEarnings.get(i).isPhysical()) {
                PhysicalResource usefulR = (PhysicalResource) totalEarnings.get(i);
                if (usefulR.getType().equals(ResType.UNKNOWN))
                    totalEarnings.remove(usefulR);
            }

        Production totalProduction = new Production(totalCosts, totalEarnings);

        if(!totalProduction.isPlayable(currentPlayer))
            throw new RetryException ("The production is unplayable.");

        currentPlayer.setTempProduction(totalProduction);

        changeState(StateName.PRODUCTION_ACTION);

        return currentState;
    }

    //FUNCTIONAL METHODS:

    /**
     * This method tries to insert the single-quantity resource into the warehouse.
     * @param resource the resource with quantity = shelf
     * @return true if it went well, false elsewhere.
     */
    private boolean singleWarehouseMove(PhysicalResource resource) {
        try {
            currentPlayer.moveIntoWarehouse(new PhysicalResource(resource.getType(),1), resource.getQuantity());
            return false;
        }
        catch(NegativeQuantityException e){
            System.err.println("System shutdown due to an internal error.");
            System.exit(1);
            return true;
        } catch (InvalidOperationException | InvalidQuantityException e) {
            return true;
        }
    }

    /**
     * This method just keeps track of the player's discounts and removes them from the costs.
     * @param discounts the discounts' map
     * @param cost the costs to filter
     */
    private static void removeDiscounts(Map<ResType, Integer> discounts, List<PhysicalResource> cost){
        for (ResType discount : discounts.keySet()) {
            int index = cost.indexOf(new PhysicalResource(discount, discounts.get(discount)));
            if(index != -1) {
                if (cost.get(index).getQuantity() > discounts.get(discount)) {
                    PhysicalResource element = cost.remove(index);
                    cost.add(new PhysicalResource(discount, element.getQuantity() - discounts.get(discount)));
                }
                else {
                    cost.remove(index);
                }
            }

        }

    }

    /**
     * This method notifies every player about the last round raise.
     */
    private void isLastRound(){
        lastRound = true;
        new LastRoundMessage("", "This is the last round").sendBroadcast(match);
    }

    /**
     * This method changes the state of the current player to the passed one and calls an update.
     * @param newState the new state
     */
    public void changeState(StateName newState){
        currentState = newState;
        currentPlayer.getSummary().updateLastUsedState(currentPlayer.getNickname(), newState);
    }

    /** Getter for current player */
    public Player getCurrentPlayer(){ return currentPlayer; }
    /** Getter for current player's state */
    public StateName getCurrentState() { return currentState; }
    /** Setter for current player's state */
    public void setCurrentState(StateName currentState) { this.currentState = currentState; }
    /** Getter for white marbles drawn */
    public int getWhiteMarbleDrawn() { return whiteMarbleDrawn; }
    /** Setter for white marbles drawn */
    public void setWhiteMarbleDrawn(int whiteMarbleDrawn) { this.whiteMarbleDrawn = whiteMarbleDrawn; }
    /** Setter for last round's flag */
    public void setLastRound(boolean lastRound){ this.lastRound = lastRound ;}
}
