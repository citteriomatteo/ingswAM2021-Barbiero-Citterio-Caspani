package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.ProductionEffect;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.ctosmessage.*;

import java.util.*;
import java.util.stream.Collectors;


public class TurnController {
    private Player currentPlayer;
    private final Player firstPlayer;
    private boolean lastRound;
    private StateName currentState;
    private final Match match;
    private final Map<String, Card> cardMap;

    public TurnController(Player firstPlayer, Match match,
                          Map<String, Card> cardMap) {
        this.lastRound = false;
        this.currentPlayer = firstPlayer;
        this.firstPlayer = firstPlayer;
        this.currentState = StateName.STARTING_TURN;
        this.match = match;
        this.cardMap = cardMap;

    }

    /**
     * This method returns a list of the accepted messages depending on the current state.
     * @return List<CtoSMessageType>
     */
    public List<CtoSMessageType> acceptedMessages(){
        if(currentState.equals(StateName.END_TURN) && !lastRound) {
            try{
                match.nextTurn();
            }
            catch(MatchEndedException e){
                lastRound = true;
                //TODO: send a message to the singlePlayer to inform him that he has lost

            }
            currentPlayer = match.getCurrentPlayer();
            currentState = StateName.STARTING_TURN;
        }
        else if(currentState.equals(StateName.END_TURN) && lastRound){
            //TODO: create a LeaderBoardMessage and send it to every player
            currentState = StateName.END_MATCH;
        }

        List<CtoSMessageType> accepted = new ArrayList<>();
        accepted.add(CtoSMessageType.SWITCH_SHELF);
        switch (currentState)
        {
            case STARTING_TURN:
                accepted.add(CtoSMessageType.LEADER_ACTIVATION);
                accepted.add(CtoSMessageType.LEADER_DISCARDING);
                accepted.add(CtoSMessageType.MARKET_DRAW);
                accepted.add(CtoSMessageType.DEV_CARD_DRAW);
                accepted.add(CtoSMessageType.PRODUCTION);
                break;

            case MARKET_ACTION:
                accepted.add(CtoSMessageType.WHITE_MARBLE_CONVERSIONS);
                break;

            case INTERMEDIATE:
                accepted.add(CtoSMessageType.WAREHOUSE_INSERTION);
                break;

            case NOT_DISCARDABLE_RESOURCES:
                accepted.add(CtoSMessageType.WAREHOUSE_INSERTION);
                accepted.add(CtoSMessageType.DISCARD_REMAINS);
                break;

            case BUY_DEV_ACTION:
                accepted.add(CtoSMessageType.PAYMENTS);
                break;

            case PLACE_DEV_CARD:
                accepted.add(CtoSMessageType.DEV_CARD_PLACEMENT);
                break;

            case PRODUCTION_ACTION:
                accepted.add(CtoSMessageType.PAYMENTS);
                break;

            case END_TURN:
                accepted.add(CtoSMessageType.LEADER_ACTIVATION);
                accepted.add(CtoSMessageType.LEADER_DISCARDING);
                accepted.add(CtoSMessageType.END_MATCH);
                break;

            case END_MATCH:
                accepted.add(CtoSMessageType.REMATCH_OFFER);
                accepted.add(CtoSMessageType.DISCONNECTION);
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

            case REMATCH_OFFER:
                accepted.add(CtoSMessageType.REMATCH_RESPONSE);
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

            default:
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

        }
        return accepted;
    }


    /**
     * This method takes the message, probably from the MatchController, checks if the message if acceptable
     * and sends it to the method that is specific for this type of message.
     * @param message the message
     * @return        true
     */
    public boolean newOperation(Message message){
        if(!message.getNickname().equals(currentPlayer.getNickname()))
            return false;

        if(!acceptedMessages().contains(message.getType()))
            return false;

        switch ((CtoSMessageType) message.getType())
        {
            case SWITCH_SHELF:
                SwitchShelfMessage shelfMessage = (SwitchShelfMessage) message;
                try {
                    currentPlayer.getPersonalBoard().getWarehouse().switchShelf(shelfMessage.getShelf1(), shelfMessage.getShelf2());
                } catch (InvalidOperationException e) {

                    //TODO: create a retry message and send it to the player
                }
                break;

            case LEADER_ACTIVATION:
                LeaderActivationMessage activationMessage = (LeaderActivationMessage) message;

                for(LeaderCard card : currentPlayer.getHandLeaders())
                    if (card.equals(cardMap.get(activationMessage.getLeader())))
                        currentPlayer.activateLeader(card);
                break;

            case LEADER_DISCARDING:
                LeaderDiscardingMessage discardingMessage = (LeaderDiscardingMessage) message;

                for(LeaderCard card : currentPlayer.getHandLeaders())
                    if (card.equals(cardMap.get(discardingMessage.getLeader())))
                        currentPlayer.discardUselessLeader(card);
                break;

            case MARKET_DRAW:
                marketDraw((MarketDrawMessage) message);
                break;

            case WHITE_MARBLE_CONVERSIONS:
                whiteMarbleConversions((WhiteMarbleConversionMessage) message);
                break;

            case WAREHOUSE_INSERTION:
                warehouseInsertion((WarehouseInsertionMessage) message);
                break;

            case DEV_CARD_DRAW:
                devCardDraw((DevCardDrawMessage) message);
                break;

            case PAYMENTS:
                payments((PaymentsMessage) message);
                break;

            case DEV_CARD_PLACEMENT:
                devCardPlacement((DevCardPlacementMessage) message);
                break;

            case PRODUCTION:
                production((ProductionMessage) message);
                break;

            case END_MATCH:

                //TODO: eventually, build the relative StoCMessage
                break;

            case REMATCH_OFFER:

                //TODO: eventually, build the relative StoCMessage
                break;

            case REMATCH_RESPONSE:

                //TODO: eventually, build the relative StoCMessage
                break;

            case DISCONNECTION:

                //TODO: eventually, build the relative StoCMessage
                break;
        }

        return true;
    }


    //METHODS FOR THE SINGLE EVENT HANDLING:

    /**
     * This method, after having received a MarketDrawMessage, does the draw from the market and decides if
     * the controller will be waiting for white marble conversions or straight warehouse insert choices.
     * @param drawMessage the message
     */
    private void marketDraw(MarketDrawMessage drawMessage){
        try {
            int whiteNumber = currentPlayer.marketDeal(drawMessage.isRow(), drawMessage.getNum());
            if(whiteNumber == 0)
                currentState = StateName.INTERMEDIATE;
            else
                currentState = StateName.MARKET_ACTION;

            //TODO: eventually, build the relative StoCMessage
        } catch (MatchEndedException e) {
            e.printStackTrace();
            //TODO: create a matchEnd message and send it to the player
        } catch (InvalidOperationException e) {
            e.printStackTrace();
            //TODO: create a retry message and send it to the player
        }

        //TODO: eventually, build the relative StoCMessage
    }

    /**
     * This method, after having received a WhiteMarbleConversionMessage, checks if the chosen conversions are made
     * available by the player's actual active leaders list.
     * Then it inserts the resources in the buffer.
     * @param whiteMarbleConversionMessage the message
     * @return true if the operation went well, false elsewhere
     */
    private boolean whiteMarbleConversions(WhiteMarbleConversionMessage whiteMarbleConversionMessage){
        for(PhysicalResource resource : whiteMarbleConversionMessage.getResources()) {
            if(! currentPlayer.getWhiteMarbleConversions().contains(resource)) {
                //TODO: create a retry message
                return false;
            }
            currentPlayer.addToWarehouse(resource);
        }
        currentState = StateName.INTERMEDIATE;
        //TODO: eventually, build the relative StoCMessage

        return true;
    }

    /** This method, after having received a WarehouseInsertionMessage, checks if the resources are insertable in
     * the chosen shelves.
     * - If something goes wrong, leaves the already inserted resources in the shelves and the controller expects a
     *   new Message with new shelf choices.
     * - If it works well, it proceeds with trying to discard resources in the buffer, if it's still not possible
     *   the controller will expect shelf choices again.
     * The process iterates until the buffer is discardable.
     * @param insertionMessage the message
     */
    private void warehouseInsertion(WarehouseInsertionMessage insertionMessage){
        List<Boolean> errors = new ArrayList<>();
        for(PhysicalResource resource : insertionMessage.getResources())
            errors.add(singleWarehouseMove(resource));
        if(errors.contains(true)){
            String errMessage = "";
            for(boolean err : errors)
                if(err)
                    errMessage+="(invalid insert choice number "+errors.indexOf(err)+")\n";
            //TODO: create a retry message with errMessage and send it to the player
            currentState = StateName.NOT_DISCARDABLE_RESOURCES;

        }
        else {
            try {
                currentPlayer.getPersonalBoard().getWarehouse().discardRemains();
            } catch (InvalidOperationException e) {
                e.printStackTrace();
                currentState = StateName.NOT_DISCARDABLE_RESOURCES;
                //TODO: create a retry message and send it to the player
            }
            currentState = StateName.END_TURN;
        }
    }

    /**
     * This method, after having received a DevCardDrawMessage, checks first if the chosen card is placeable and buyable.
     * Then, he takes the card and puts it in the TempDevCard slot (not paid yet).
     * @param devCardDrawMessage the message
     */
    private void devCardDraw(DevCardDrawMessage devCardDrawMessage){
        try {
            if(!match.getCardGrid().isBuyable(currentPlayer,devCardDrawMessage.getRow(),
                    devCardDrawMessage.getColumn())) {
                //TODO: create a retry message and send it to the player
            }

            else if(!currentPlayer.verifyPlaceability((devCardDrawMessage.getRow()))) {
                //TODO: create a retry message and send it to the player
            }
            else {
                currentPlayer.drawDevelopmentCard(devCardDrawMessage.getRow(),devCardDrawMessage.getColumn());
                currentState = StateName.BUY_DEV_ACTION;

            }
            //TODO: eventually, build the relative StoCMessage

        } catch (InvalidCardRequestException e) {
            //TODO: create a retry message and send it to the player
        } catch (NoMoreCardsException e) {
            //TODO: create a retry message and send it to the player

        } catch (MatchEndedException e) {
            lastRound = true;
        }
    }

    /**
     * This method is used in production/devCard payments and, after having receiver a PaymentsMessage,
     *   clones the warehouse and strongbox states before payments and tries to pay everything.
     * If something goes wrong, it refreshes the old storage versions.
     * Does an addictional control for the production payment, summing the resources by type for a simple
     *   no-order "equals" operation with the TempProduction and then, eventually, produces it.
     * @param paymentsMessage the message
     */
    private void payments(PaymentsMessage paymentsMessage){
        StrongBox sbUndo = StrongBox.clone(currentPlayer.getPersonalBoard().getStrongBox());
        Warehouse whUndo = WarehouseDecorator.clone(currentPlayer.getPersonalBoard().getWarehouse());

        if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()){

            List<PhysicalResource> payments = new ArrayList<>();
            payments.addAll(paymentsMessage.getStrongboxCosts());
            payments.addAll(paymentsMessage.getWarehouseCosts().values());

            for(PhysicalResource r : payments)
                for(PhysicalResource r1 : payments)
                    if(r.equals(r1)) {
                        int quantity = r.getQuantity()+r1.getQuantity();
                        payments.remove(r); payments.remove(r1);
                        try {
                            payments.add(new PhysicalResource(r.getType(), quantity));
                        } catch (NegativeQuantityException e) { e.printStackTrace(); System.exit(1); }
                    }

            //checking if the list of payments equals to the one chosen before (in tempProduction):
            if(!(new HashSet<>(payments).equals(new HashSet<>(currentPlayer.getTempProduction().getCost())))) {
                //TODO: create a retry message and send it to the player
            }
        }

        for(PhysicalResource r : paymentsMessage.getStrongboxCosts())
            try {
                currentPlayer.payFromStrongbox(r);
            } catch (NotEnoughResourcesException e) {
                //Re-inserting resources in the Strongbox
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
            }

        for(PhysicalResource r : paymentsMessage.getWarehouseCosts().values()) {
            try {
                currentPlayer.payFromWarehouse(r,getKeyByValue(paymentsMessage.getWarehouseCosts(),r));
            } catch (InvalidOperationException e) {
                currentPlayer.getPersonalBoard().setWarehouse(whUndo);
                currentPlayer.getPersonalBoard().setStrongBox(sbUndo);
                //TODO: create a retry message
            }
        }

        if(currentState.getVal() == StateName.BUY_DEV_ACTION.getVal())
            currentState = StateName.PLACE_DEV_CARD;
        else if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()) {
            try {
                currentPlayer.getTempProduction().produce(currentPlayer);
                currentPlayer.setTempProduction(null);
            } catch (MatchEndedException e) {
                e.printStackTrace();
                lastRound = true;
                //TODO: create a MatchEndedMessage and send it to every player.
            }
            currentState = StateName.END_TURN;
        }
        //TODO: eventually, build the relative StoCMessage
    }

    /**
     * This method, after having received a DevCardPlacementMessage, tries to insert the card in TempDevCard and
     * updates sets the new state to END_TURN.
     * @param devCardPlacementMessage the message
     */
    public void devCardPlacement(DevCardPlacementMessage devCardPlacementMessage){
        try {
            currentPlayer.insertDevelopmentCard(devCardPlacementMessage.getColumn());
        } catch (MatchEndedException e) {
            lastRound = true;
            //TODO: create a MatchEndedMessage and send it to every player.
        } catch (InvalidOperationException e) {
            currentPlayer.setTempDevCard(null);
            //TODO: create a RetryMessage and send it to the player.
        }

        //TODO: eventually, build the relative StoCMessage
        currentState = StateName.END_TURN;
    }

    /**
     * This method, after having received a ProductionMessage, checks:
     * - if the player has all the cards that he wants to produce;
     * - checks if the number of unknown costs and earnings corresponds to the chosen cards unknown quantities;
     * and aborts the operation in both cases, if something went wrong.
     * Then, this method correctly builds the temporary production and insert it into tempProduction (for next).
     * @param productionMessage the message
     * @return true if the operation went well, false elsewhere.
     */
    public boolean production(ProductionMessage productionMessage){
        //checking if the player has all the cards that he wants to produce
        boolean basicProdFound=false;
        List<Boolean> cardsErrors = new ArrayList<>();
        for(String id : productionMessage.getCardIds())
            if(!id.equals("BASICPROD"))
                cardsErrors.add(!(currentPlayer.getPersonalBoard().getDevCardSlots().getTop().contains(cardMap.get(id)) ||
                        currentPlayer.getPersonalBoard().getActiveProductionLeaders().contains(cardMap.get(id))));
            else {
                basicProdFound = true;
                productionMessage.getCardIds().remove("BASICPROD");
            }

        if(cardsErrors.contains(true)) {
            String errMessage = "";
            for (boolean err : cardsErrors)
                if (err)
                    errMessage = errMessage + "(invalid insert choice number " + cardsErrors.indexOf(err) + ")\n";
            //TODO: create a retry message with errMessage and send it to the player
            return false;
        }

        //checks if the number of unknown costs and earnings corresponds to the chosen cards unknown quantities
        int uCosts=0, uEarnings=0;
        if(basicProdFound) {

            uCosts += currentPlayer.getPersonalBoard().getBasicProduction().getCost().stream().filter((x)->x.getType().equals(ResType.UNKNOWN)).count();
            for(Resource r : currentPlayer.getPersonalBoard().getBasicProduction().getEarnings())
                if(r.isPhysical()){
                    PhysicalResource usefulR = (PhysicalResource) r;
                    uEarnings += (usefulR.getType().equals(ResType.UNKNOWN) ? 1 : 0);
                }
        }
        for(String id : productionMessage.getCardIds()){
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            uCosts += prod.getCost().stream().filter((x)->x.getType().equals(ResType.UNKNOWN)).count();
            for(Resource r : prod.getEarnings())
                if(r.isPhysical()){
                    PhysicalResource usefulR = (PhysicalResource) r;
                    uEarnings += (usefulR.getType().equals(ResType.UNKNOWN) ? 1 : 0);
                }
        }

        if(uCosts != productionMessage.getProductionOfUnknown().getCost().size() ||
                uEarnings != productionMessage.getProductionOfUnknown().getEarnings().size()) {
            //TODO: create a retry message and send it to the player
            return false;
        }

        //create the tempProduction
        List<PhysicalResource> totalCosts = new ArrayList<>(productionMessage.getProductionOfUnknown().getCost());
        List<Resource> totalEarnings = new ArrayList<>(productionMessage.getProductionOfUnknown().getEarnings());

        for(String id : productionMessage.getCardIds()) {
            Production prod;
            if(!cardMap.get(id).isLeader())
                prod = ((DevelopmentCard) cardMap.get(id)).getProduction();
            else
                prod = ((ProductionEffect) ((LeaderCard)cardMap.get(id)).getEffect()).getProduction();
            totalCosts.addAll(prod.getCost());
            totalEarnings.addAll(prod.getEarnings());
        }

        totalCosts.stream().filter((x)->!x.getType().equals(ResType.UNKNOWN)).collect(Collectors.toList());
        for(Resource r : totalEarnings)
            if(r.isPhysical()) {
                PhysicalResource usefulR = (PhysicalResource) r;
                if (usefulR.getType().equals(ResType.UNKNOWN))
                    totalEarnings.remove(usefulR);
            }

        Production totalProduction = new Production(totalCosts, totalEarnings);

        if(!totalProduction.isPlayable(currentPlayer)){
            //TODO: create a RetryMessage and send it to the player
            return false;
        }
        currentPlayer.setTempProduction(totalProduction);

        //TODO: eventually, build the relative StoCMessage
        currentState = StateName.PRODUCTION_ACTION;

        return true;
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

}
