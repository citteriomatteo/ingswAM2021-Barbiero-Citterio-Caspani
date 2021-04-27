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
import it.polimi.ingsw.network.message.stocmessage.TurnStartedMessage;

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

    public List<CtoSMessageType> acceptedStates(){
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


    public boolean newOperation(Message message){
        if(!message.getNickname().equals(currentPlayer.getNickname()))
            return false;

        if(!acceptedStates().contains(message.getType()))
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
                MarketDrawMessage drawMessage = (MarketDrawMessage) message;
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

                break;

            case WHITE_MARBLE_CONVERSIONS:
                WhiteMarbleConversionMessage whiteMarbleConversionMessage = (WhiteMarbleConversionMessage) message;

                for(PhysicalResource resource : whiteMarbleConversionMessage.getResources()) {
                    if(! currentPlayer.getWhiteMarbleConversions().contains(resource)) {
                        //TODO: create a retry message
                        break;
                    }

                    currentPlayer.addToWarehouse(resource);
                }

                currentState = StateName.INTERMEDIATE;
                //TODO: eventually, build the relative StoCMessage
                break;

            case WAREHOUSE_INSERTION:
                WarehouseInsertionMessage insertionMessage = (WarehouseInsertionMessage) message;

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
                break;

            case DEV_CARD_DRAW:
                DevCardDrawMessage devCardDrawMessage = (DevCardDrawMessage) message;
                try {
                    if(!match.getCardGrid().isBuyable(currentPlayer,devCardDrawMessage.getLevel(),
                            devCardDrawMessage.getColor())) {
                        //TODO: create a retry message and send it to the player
                    }

                    else if(!currentPlayer.verifyPlaceability((devCardDrawMessage.getLevel()))) {
                        //TODO: create a retry message and send it to the player
                    }
                    else {
                        currentPlayer.drawDevelopmentCard(devCardDrawMessage.getLevel(),devCardDrawMessage.getColor());
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
                break;

            case PAYMENTS:
                PaymentsMessage paymentsMessage = (PaymentsMessage) message;
                StrongBox sbUndo = StrongBox.clone(currentPlayer.getPersonalBoard().getStrongBox());
                Warehouse whUndo = WarehouseDecorator.clone(currentPlayer.getPersonalBoard().getWarehouse());

                if(currentState.getVal() == StateName.PRODUCTION_ACTION.getVal()){

                    List<PhysicalResource> payments = new ArrayList<>();
                    payments.addAll(((PaymentsMessage) message).getStrongboxCosts());
                    payments.addAll(((PaymentsMessage) message).getWarehouseCosts().values());

                    for(PhysicalResource r : payments)
                        for(PhysicalResource r1 : payments)
                            if(r.equals(r1)) {
                                int quantity = r.getQuantity()+r1.getQuantity();
                                payments.remove(r); payments.remove(r1);
                                try {
                                    payments.add(new PhysicalResource(r.getType(), quantity));
                                } catch (NegativeQuantityException e) { e.printStackTrace();System.exit(1); }
                            }

                    //checking if the list of payments equals to the one chosen before (in tempProduction):
                    if(!(new HashSet<>(payments).equals(new HashSet<>(currentPlayer.getTempProduction().getCost())))) {
                        //TODO: create a retry message and send it to the player
                        break;
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
                        currentPlayer.payFromWarehouse(r,getKeyByValue(((PaymentsMessage) message).getWarehouseCosts(),r));
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
                break;

            case DEV_CARD_PLACEMENT:
                DevCardPlacementMessage m = (DevCardPlacementMessage) message;

                try {
                    currentPlayer.insertDevelopmentCard( m.getColumn());
                } catch (MatchEndedException e) {
                    lastRound = true;
                    //TODO: create a MatchEndedMessage and send it to every player.
                } catch (InvalidOperationException e) {
                    //TODO: create a RetryMessage and send it to the player.
                }

                //TODO: eventually, build the relative StoCMessage
                currentState = StateName.END_TURN;
                break;

            case PRODUCTION:
                ProductionMessage productionMessage = (ProductionMessage) message;

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
                    break;
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
                    break;
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
                }
                currentPlayer.setTempProduction(totalProduction);

                //TODO: eventually, build the relative StoCMessage
                currentState = StateName.PRODUCTION_ACTION;
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

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
