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

import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static java.util.Map.entry;


public class ClientController
{
    private LightMatch match;
    private View view;      //LightMatch is observable by the View -> it has a View reference on its superclass -> MAYBE no need to put this also here!
    private StateName currentState;
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
                entry(StateName.REMATCH_OFFER, List.of(REMATCH))
        );
    }

    public ClientController(View view){
        this.view = view;
    }

    //updato lo stato corrente e controllo: se si tratta dello stesso di prima, allora mi è arrivata un Retry,
    //      altrimenti sarà un NextState.
    public void updateCurrentState(StoCMessage msg){
        System.out.println("new state: " + msg.getType());
        if(msg.getType().equals(StoCMessageType.RETRY)) {
            this.currentState = ((RetryMessage) msg).getCurrentState();
            printRetry("Invalid message: Retry. Type 'help' to view the map of commands.");

        }

        else if(msg.getType().equals(StoCMessageType.NEXT_STATE)){
            this.currentState = ((NextStateMessage) msg).getNewState();
            if(match != null)
                match.getPlayerSummary(msg.getNickname()).setLastUsedState(currentState);
            /*if(match != null) {
                if (!currentState.equals(StateName.WAITING_FOR_TURN)) {
                    if (currentState.equals(StateName.STARTING_TURN)
                            && !List.of(StateName.WAITING_FOR_TURN, StateName.END_TURN).contains(match.getPlayerSummary(msg.getNickname()).getLastUsedState()))
                        currentState = match.getPlayerSummary(msg.getNickname()).getLastUsedState();
                } else
                    match.getPlayerSummary(msg.getNickname()).setLastUsedState(currentState);
                System.out.println("" + currentState);
            }*/
        }

        printMoveLegend(msg);

    }

    //quando si ha una retry (da parte del server o dalla keyboardReader) viene chiamata questa, che stampa l'errore.
    public void printRetry(String errMessage){
        view.printRetry(errMessage);
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

    /*
    Quando si ha un NextStateMessage dal server, si chiama questa funzione che printa l'introduzione allo stato in cui si è
     (cosa e come può scrivere, frasi del tipo "Scegli una mossa per il tuo turno:", ....)
     Pensare se ha più senso che questa funzione sia scritta qui o singolarmente sulle due View.
     Eventualmente, tutte le funzioni draw...() saranno chiamate sulla View (gui o cli che sia).
     */
    public void printMoveLegend(StoCMessage msg){

        switch(currentState){
            case LOGIN:
                view.printTitle();
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
            case WAITING:
                view.drawWaitingLayout();
                break;
            case START_GAME:
                view.drawLeadersChoiceLayout();
                break;
            case WAITING_LEADERS:
                //view.drawLeadersChoiceLayout();
                break;
            case WAITING_RESOURCES:
                view.drawResourcesChoiceLayout();
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
        for(String id : cards) {
            Card chosen = match.getCardMap().get(id.toUpperCase());
            if(chosen != null)
                view.drawCard(chosen, id);
        }

        return true;

    }
    public LightMatch getMatch(){ return match; }

    public StateName getCurrentState() { return currentState; }

}
