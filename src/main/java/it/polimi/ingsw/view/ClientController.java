package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.lightmodel.LightMatch;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static java.util.Map.entry;


public class ClientController
{
    private LightMatch match;
    private final View view;      //LightMatch is observable by the View -> it has a View reference on its superclass -> MAYBE no need to put this also here!
    private StateName currentState;
    private String nickname;
    private static final Map<StateName, List<CtoSMessageType>> acceptedMessagesMap;
    static {
        acceptedMessagesMap = Map.ofEntries(
                entry(StateName.LOGIN, List.of(LOGIN)),
                entry(StateName.RECONNECTION, List.of(BINARY_SELECTION)),
                entry(StateName.NEW_PLAYER, List.of(BINARY_SELECTION)),
                entry(StateName.NUMBER_OF_PLAYERS, List.of(NUM_PLAYERS)),
                entry(StateName.SP_CONFIGURATION_CHOOSE, List.of(BINARY_SELECTION)),
                entry(StateName.MP_CONFIGURATION_CHOOSE, List.of(BINARY_SELECTION)),
                entry(StateName.CONFIGURATION, List.of(CONFIGURE)),
                entry(StateName.WAITING,List.of(PING)),
                entry(StateName.WAITING_FOR_PLAYERS, List.of(PING)),
                entry(StateName.START_GAME, List.of(PING,LEADERS_CHOICE)),
                entry(StateName.WAITING_LEADERS, List.of(LEADERS_CHOICE)),
                entry(StateName.WAITING_RESOURCES, List.of(STARTING_RESOURCES)),
                entry(StateName.STARTING_PHASE_DONE,List.of(PING)),
                entry(StateName.WAITING_FOR_TURN, List.of(PING)),
                entry(StateName.STARTING_TURN, List.of(LEADER_ACTIVATION, LEADER_DISCARDING, SWITCH_SHELF,
                        MARKET_DRAW, DEV_CARD_DRAW, PRODUCTION)),
                entry(StateName.MARKET_ACTION, List.of(WHITE_MARBLE_CONVERSIONS, SWITCH_SHELF)),
                entry(StateName.RESOURCES_PLACEMENT, List.of(WAREHOUSE_INSERTION, SWITCH_SHELF)),
                entry(StateName.BUY_DEV_ACTION, List.of(SWITCH_SHELF, PAYMENTS)),
                entry(StateName.PLACE_DEV_CARD, List.of(SWITCH_SHELF, DEV_CARD_PLACEMENT)),
                entry(StateName.PRODUCTION_ACTION, List.of(SWITCH_SHELF, PAYMENTS)),
                entry(StateName.END_TURN, List.of(SWITCH_SHELF, LEADER_ACTIVATION, LEADER_DISCARDING, END_TURN)),
                entry(StateName.END_MATCH, List.of(REMATCH, DISCONNECTION)),
                entry(StateName.REMATCH_OFFER, List.of(REMATCH, DISCONNECTION))
        );
    }

    public ClientController(View view){
        this.view = view;
        view.printTitle();
        currentState = StateName.LOGIN;
    }


    public void updateCurrentState(StoCMessage msg){
        if(msg.getType().equals(StoCMessageType.RETRY)) {
            RetryMessage rMsg= (RetryMessage)msg;
            this.currentState = rMsg.getCurrentState();
            printMoveLegend(msg);
            printRetry(rMsg.getErrorMessage(), rMsg.getCurrentState());
        }

        else if(msg.getType().equals(StoCMessageType.NEXT_STATE)){
            this.currentState = ((NextStateMessage) msg).getNewState();
            //in order to remove the frame around the boards when the match restarts
            if(((NextStateMessage) msg).getNewState().equals(StateName.WAITING_LEADERS))
                view.setLastRound(false);
            printMoveLegend(msg);
            if(match != null && !((NextStateMessage) msg).getNewState().equals(StateName.END_MATCH))
                view.showAll(match); //todo: TO REMOVE

        }

    }

    //quando si ha una retry (da parte del server o dalla keyboardReader) viene chiamata questa, che aggiorna la lastLayout.
    public void printRetry(String errMessage, StateName currentState){
        view.printRetry(errMessage, currentState, match);
    }

    public void printMatchResults(String message, Map<String, Integer> ranking){
        view.printMatchResults(message, ranking);

    }

    public void printLastRound(){
        view.printLastRound();
    }

    public void printTokenDraw(String tokenName, int remainingTokens){
        view.printTokenDraw(tokenName, remainingTokens);
    }


    public void printMoveLegend(StoCMessage msg){

        switch(currentState){
            case LOGIN:
                view.drawLoginLayout();
                break;
            case RECONNECTION:
                view.drawReconnectionLayout();
                break;
            case NEW_PLAYER:
                view.drawNewPlayerLayout();
                break;
            case NUMBER_OF_PLAYERS:
                view.drawNumPlayersLayout();
                break;
            case SP_CONFIGURATION_CHOOSE:
            case MP_CONFIGURATION_CHOOSE:
                view.drawConfigurationChoice();
                break;
            case CONFIGURATION:
                view.drawConfigurationLayout();
                break;
            case STARTING_PHASE_DONE:
            case WAITING_FOR_PLAYERS:
            case WAITING:
                view.drawWaitingLayout();
                break;
            case START_GAME:
                break;
            case WAITING_LEADERS:
                view.drawLeadersChoiceLayout();
                break;
            case WAITING_RESOURCES:
                view.drawResourcesChoiceLayout(match.positionOf(nickname));
                break;
            case WAITING_FOR_TURN:
                view.drawYourTurnLayout(false);
                break;
            case STARTING_TURN:
                view.drawYourTurnLayout(true);
                break;
            case MARKET_ACTION:
                view.drawWhiteMarbleConversionsLayout();
                break;
            case RESOURCES_PLACEMENT:
                view.drawResPlacementLayout();
                break;
            case BUY_DEV_ACTION:
                view.drawBuyDevCardLayout();
                break;
            case PLACE_DEV_CARD:
                view.drawPlaceDevCardLayout();
                break;
            case PRODUCTION_ACTION:
                view.drawProductionLayout();
                break;
            case END_TURN:
                view.drawEndTurnLayout();
                break;
            case END_MATCH:
                view.drawEndMatchLayout();
                break;
            case REMATCH_OFFER:
                view.drawRematchOfferLayout(msg.getNickname());
                break;
        }

    }


    public boolean isAccepted(CtoSMessageType type){
        if (type == DISCONNECTION)
            return true;
        return acceptedMessagesMap.get(currentState).contains(type);
    }

    public void setLightMatch(Summary summary){
        match = new LightMatch(summary, this.view);
    }

    public boolean printCardInfo(String request){
        List<String> splitRequest = new ArrayList(Arrays.asList(request.split("\\s+")));
        splitRequest.remove(0);
        if(splitRequest.size() != 1)
            return false;
        List<String> cards = new ArrayList(Arrays.asList(splitRequest.get(0).split(",")));
        for(int i = 0; i < cards.size(); i++) {
            Card chosen = LightMatch.getCardMap().get(cards.get(i).toUpperCase());
            if(chosen == null){
                System.out.println("Card " + cards.get(i).toUpperCase() + " does not exist.");
                cards.remove(cards.get(i));
            }
        }
        view.drawCards(cards.stream().map(String::toUpperCase).collect(Collectors.toList()));

        return true;
    }

    public boolean printRematchOffer(String message){
        view.drawRematchOfferLayout(message);
        return true;
    }

    public boolean printDiscountMap(String nickname){
        view.printDiscountMap(match.getLightPlayer(nickname));
        return true;
    }
    public boolean printWhiteMarbleConversions(String nickname){
        view.printWhiteMarbleConversions(match.getLightPlayer(nickname));
        return true;
    }
    public LightMatch getMatch(){ return match; }
    public StateName getCurrentState() { return currentState; }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
