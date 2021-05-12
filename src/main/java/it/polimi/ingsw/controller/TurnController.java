package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.ProductionEffect;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.*;
import java.util.stream.Collectors;


public class TurnController {
    private Player currentPlayer;
    private final Player firstPlayer;
    private boolean lastRound;
    private StateName currentState;
    private final Match match;
    private final Map<String, Card> cardMap;
    private int whiteMarbleDrawn;
    private String bufferResult="";

    public TurnController(Match match, Map<String, Card> cardMap) {
        this.lastRound = false;
        this.currentPlayer = match.getCurrentPlayer();
        this.firstPlayer = match.getCurrentPlayer();
        this.currentState = StateName.STARTING_TURN;
        this.match = match;
        this.cardMap = cardMap;
        this.whiteMarbleDrawn = 0;

        //initializing the observer and setting it to every player's observable instance.
        ModelObserver obs = new Summary(this.match, this.cardMap);
        for(Player p : match.getPlayers())
            p.setSummary(obs);
    }

    /**
     * This method returns a list of the accepted messages depending on the current state.
     * @return List<CtoSMessageType>
     */
    public StateName nextTurn() throws MatchEndedException {
        //If it's the last turn of the last player of the last round...
        if(currentPlayer.equals(match.getPlayers().get(match.getPlayers().size()-1)) && currentState.equals(StateName.END_TURN) && lastRound){
            Map<String, Integer> ranking = new HashMap<>();
            for(Player p : match.getPlayers())
                ranking.put(p.getNickname(), p.totalWinPoints());
            throw new MatchEndedException(bufferResult+"\nMatch Ranking:", ranking);
        }

        else if(currentState.equals(StateName.END_TURN)) {
                try{
                    match.nextTurn();
                }
                catch(LastRoundException e){
                    Map<String, Integer> myScore = new HashMap<>();
                    myScore.put(currentPlayer.getNickname(), currentPlayer.totalWinPoints());
                    throw new MatchEndedException("You lost!", myScore);
                }
                currentPlayer = match.getCurrentPlayer();
                currentState = StateName.STARTING_TURN;
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
            currentPlayer.getPersonalBoard().getWarehouse().switchShelf(shelf1, shelf2);
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
                currentPlayer.activateLeader(card);
                return currentState;
            }
        throw new RetryException ("Invalid leader activation attempt.");
    }

    /**
     * This method does the activate of the chosen leader.
     * @param leaderId is the id of the leader card
     * @return the new State of the controller and client
     * @throws RetryException
     */
    public StateName leaderDiscarding(String leaderId) throws RetryException {
        for(LeaderCard card : currentPlayer.getHandLeaders())
            if (card.equals(cardMap.get(leaderId))) {
                currentPlayer.discardUselessLeader(card);
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
     * @throws RetryException
     */
    public StateName marketDraw(boolean row, int num) throws RetryException {
        try {
            whiteMarbleDrawn = currentPlayer.marketDeal(row, num);
            if(whiteMarbleDrawn == 0)
                currentState = StateName.RESOURCES_PLACEMENT;
            else
                currentState = StateName.MARKET_ACTION;

        } catch (LastRoundException e) { lastRound = true; }
        catch (InvalidOperationException e) {
            throw new RetryException ("Invalid market parameters.");
        }
        return currentState;
    }

    /**
     * This method checks if the chosen conversions are made available by the player's actual active leaders list.
     * Then it inserts the resources in the buffer.
     * @param resources the conversions
     * @return the new State of the controller and client
     * @throws RetryException
     */
    public StateName whiteMarblesConversion(List<PhysicalResource> resources) throws RetryException {
        if(resources.size() != whiteMarbleDrawn) {
            throw new RetryException ("Invalid conversions.");
        }
        for(PhysicalResource resource : resources) {
            boolean found=false;
            for(PhysicalResource resource1 : currentPlayer.getWhiteMarbleConversions())
                if(resource1.equals(resource) && !found) {
                    currentPlayer.addToWarehouse(resource);
                    found=true;
                    whiteMarbleDrawn--;
                }
            if(!found)
                throw new RetryException ("Invalid conversions.");

        }
        if(whiteMarbleDrawn==0)
            currentState = StateName.RESOURCES_PLACEMENT;

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
     * @throws RetryException
     */
    public StateName warehouseInsertion(List<PhysicalResource> resources) throws RetryException {
        List<Boolean> errors = new ArrayList<>();
        for(PhysicalResource resource : resources)
            errors.add(singleWarehouseMove(resource));
        if(errors.contains(true)){
            String errMessage = "";
            for(boolean err : errors)
                if(err)
                    errMessage+="(invalid insert choice number "+errors.indexOf(err)+")\n";
            throw new RetryException (errMessage);
        }
        else {
            try {
                currentPlayer.getPersonalBoard().getWarehouse().discardRemains();
            } catch (InvalidOperationException e) {
                currentState = StateName.RESOURCES_PLACEMENT;
                throw new RetryException ("You can still place other resources.");
            }
            currentState = StateName.END_TURN;
        }

        return currentState;
    }

    /**
     * This method, after having received a DevCardDrawMessage, checks first if the chosen card is placeable and buyable.
     * Then, he takes the card and puts it in the TempDevCard slot (not paid yet).
     * @param row    the row
     * @param column the column
     * @return       the new State of the controller and client
     * @throws RetryException
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
                currentState = StateName.BUY_DEV_ACTION;
            }
        } catch (InvalidCardRequestException e) {
            throw new RetryException ("Invalid development card draw parameters.");
        } catch (NoMoreCardsException e) {
            throw new RetryException ("The selected cell is empty.");
        }
        catch (LastRoundException e) {
            lastRound = true;
            bufferResult = "Last card of the column has been drawn. You lost!";
        }

        return currentState;
    }

    /**
     * This method is used in production/devCard payments and
     *   clones the warehouse and strongbox states before payments and tries to pay everything.
     * If something goes wrong, it refreshes the old storage versions.
     * Does an addictional control for the production payment, summing the resources by type for a simple
     *   no-order "equals" operation with the TempProduction and then, eventually, produces it.
     * @param strongboxCosts the list of resources for the Strongbox
     * @param warehouseCosts the map of resources to insert in the warehouse
     * @return               the new State of the controller and client
     * @throws RetryException
     */
    public StateName payments(List<PhysicalResource> strongboxCosts, Map<Integer, PhysicalResource> warehouseCosts) throws RetryException {
        StrongBox sbUndo = StrongBox.clone(currentPlayer.getPersonalBoard().getStrongBox());
        Warehouse whUndo = WarehouseDecorator.clone(currentPlayer.getPersonalBoard().getWarehouse());

        if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()){

            List<PhysicalResource> payments = new ArrayList<>();
            payments.addAll(strongboxCosts);
            payments.addAll(warehouseCosts.values());

            for(int i= 0; i< payments.size(); i++)
                for(int j=i+1; j<payments.size(); j++)
                    if(payments.get(i).equals(payments.get(j))) {
                        PhysicalResource r = payments.get(i);
                        PhysicalResource r1 = payments.get(j);
                        int quantity = r.getQuantity()+r1.getQuantity();
                        payments.remove(r); payments.remove(r1);
                        try {
                            payments.add(new PhysicalResource(r.getType(), quantity));
                        } catch (NegativeQuantityException e) { System.exit(1); }
                    }

            //checking if the list of payments equals to the one chosen before (in tempProduction):
            /*
            if(!((new HashSet<>(payments)).equals(new HashSet<>(currentPlayer.getTempProduction().getCost())))) {
                throw new RetryException ("Payments don't match the chosen production ones.");
            }
             */

            if(payments.size() != currentPlayer.getTempProduction().getCost().size())
                throw new RetryException("Payments don't match the chosen production ones.");
            boolean found = false;
            for (PhysicalResource p : payments) {
                for (PhysicalResource p1 : currentPlayer.getTempProduction().getCost())
                    if (p.equals(p1) && p.getQuantity()== p1.getQuantity())
                        found = true;

                if(!found)
                    throw new RetryException("Payments don't match the chosen production ones.");
            }
        }

        for(PhysicalResource r : strongboxCosts)
            try {
                currentPlayer.payFromStrongbox(r);
            } catch (NotEnoughResourcesException e) {
                //Re-inserting resources in the Strongbox
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
                throw new RetryException ("Not enough resources in the strongbox.");
            }

        for(PhysicalResource r : warehouseCosts.values()) {
            try {
                currentPlayer.payFromWarehouse(r,getKeyByValue(warehouseCosts,r));
            } catch (InvalidOperationException e) {
                currentPlayer.getPersonalBoard().setWarehouse(whUndo);
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
                throw new RetryException ("Invalid shelves choice.");
            }
        }

        if(currentState.getVal() == StateName.BUY_DEV_ACTION.getVal())
            currentState = StateName.PLACE_DEV_CARD;
        else if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()) {
            try {
                currentPlayer.getTempProduction().produce(currentPlayer);
                currentPlayer.setTempProduction(null);
            } catch (LastRoundException e) { lastRound = true; }
            currentState = StateName.END_TURN;
        }
        return currentState;
    }

    /**
     * This method, after having received a DevCardPlacementMessage, tries to insert the card in TempDevCard and
     * updates sets the new state to END_TURN.
     * @param column    the chosen dev slot
     * @return the new State of the controller and client
     * @throws RetryException
     */
    public StateName devCardPlacement(int column) throws RetryException {
        try {
            currentPlayer.insertDevelopmentCard(column);
        } catch (LastRoundException e) {
            lastRound = true;
            if(match.getPlayers().size()==1)
                bufferResult = "You bought the 7th card. You won!";
        }
        catch (InvalidOperationException e) {
            currentPlayer.setTempDevCard(null);
            throw new RetryException ("Invalid placement parameters.");
        }
        currentState = StateName.END_TURN;
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
     * @throws RetryException
     */
    public StateName production(List<String> cardIds, Production productionOfUnknown) throws RetryException {
        //checking if the player has all the cards that he wants to produce
        boolean basicProdFound=false;
        List<Boolean> cardsErrors = new ArrayList<>();
        for(int i=0; i < cardIds.size(); i++)
            if(!cardIds.get(i).equals("BASICPROD")) {
                String id = cardIds.get(i);
                cardsErrors.add(!(currentPlayer.getPersonalBoard().getDevCardSlots().getTop().contains(cardMap.get(id)) ||
                        currentPlayer.getPersonalBoard().getActiveProductionLeaders().contains(cardMap.get(id))));
            }
            else {
                basicProdFound = true;
                cardIds.remove("BASICPROD");
            }

        if(cardsErrors.contains(true)) {
            String errMessage = "";
            for (boolean err : cardsErrors)
                if (err)
                    errMessage = errMessage + "(invalid insert choice number " + cardsErrors.indexOf(err) + ")\n";
            throw new RetryException (errMessage);
        }

        //checks if the number of unknown costs and earnings corresponds to the chosen cards unknown quantities
        int uCosts=0, uEarnings=0;
        if(basicProdFound) {

            uCosts += currentPlayer.getPersonalBoard().getBasicProduction().getCost().stream().filter((x)->x.getType().equals(ResType.UNKNOWN)).map((x)->x.getQuantity()).reduce(0, (x,y)->x+y);
            for(Resource r : currentPlayer.getPersonalBoard().getBasicProduction().getEarnings())
                if(r.isPhysical()){
                    PhysicalResource usefulR = (PhysicalResource) r;
                    uEarnings += (usefulR.getType().equals(ResType.UNKNOWN) ? usefulR.getQuantity() : 0);
                }
        }
        for(String id : cardIds){
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            uCosts += prod.getCost().stream().filter((x)->x.getType().equals(ResType.UNKNOWN)).map((x)->x.getQuantity()).reduce(0, (x,y)->x+y);
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

        for(String id : cardIds) {
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            totalCosts.addAll(prod.getCost());
            totalEarnings.addAll(prod.getEarnings());
        }

        totalCosts = totalCosts.stream().filter((x)->(!x.getType().equals(ResType.UNKNOWN))).collect(Collectors.toList());

        for(Resource r : totalEarnings)
            if(r.isPhysical()) {
                PhysicalResource usefulR = (PhysicalResource) r;
                if (usefulR.getType().equals(ResType.UNKNOWN))
                    totalEarnings.remove(usefulR);
            }

        Production totalProduction = new Production(totalCosts, totalEarnings);

        if(!totalProduction.isPlayable(currentPlayer))
            throw new RetryException ("The production is unplayable.");

        currentPlayer.setTempProduction(totalProduction);

        currentState = StateName.PRODUCTION_ACTION;

        return currentState;
    }

    //FUNCTIONAL METHODS:

    /**
     * This method tries to insert the single-quantity resource into the warehouse.
     * @param resource the resource with quantity = shelf
     * @return true if it went well, false elsewhere.
     */
    public boolean singleWarehouseMove(PhysicalResource resource){
        try {
            currentPlayer.moveIntoWarehouse(new PhysicalResource(resource.getType(),1), resource.getQuantity());
            return false;
        }
        catch(NegativeQuantityException e){
            System.err.println("System shutdown due to an internal error.");
            System.exit(1);
            return true;
        } catch (InvalidOperationException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * This method gets the key that corresponds to a specific value in the map, if present.
     * @param map   the map
     * @param value the value
     * @param <T>   the keys type
     * @param <E>   the values type
     * @return      true if ok, false elsewhere
     */
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Player getCurrentPlayer(){ return currentPlayer; }
    public StateName getCurrentState() { return currentState; }
    public void setCurrentState(StateName currentState) { this.currentState = currentState; }
    public int getWhiteMarbleDrawn() { return whiteMarbleDrawn; }
    public void setWhiteMarbleDrawn(int whiteMarbleDrawn) { this.whiteMarbleDrawn = whiteMarbleDrawn; }
    public void setLastRound(boolean lastRound){ this.lastRound = lastRound ;}
}
