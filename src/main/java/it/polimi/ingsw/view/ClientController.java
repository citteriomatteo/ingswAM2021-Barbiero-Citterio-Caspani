package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.lightmodel.LightMatch;

import java.util.List;
import java.util.Map;

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
                entry(StateName.START_GAME, List.of(PING)),
                entry(StateName.WAITING_LEADERS, List.of(LEADERS_CHOICE)),
                entry(StateName.WAITING_RESOURCES, List.of(STARTING_RESOURCES)),
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
        if(msg.getType().equals(StoCMessageType.RETRY)) {
            printRetry("Invalid message: Retry. Type 'help' to view the map of commands.");
        }
        else if(msg.getType().equals(StoCMessageType.NEXT_STATE)){
            this.currentState = ((NextStateMessage) msg).getNewState();
        }
        printMoveLegend(msg);
    }

    //quando si ha una retry (da parte del server o dalla keyboardReader) viene chiamata questa, che stampa l'errore.
    public void printRetry(String errMessage){
        System.out.println(errMessage);
    }

    /*
    Quando si ha un NextStateMessage dal server, si chiama questa funzione che printa l'introduzione allo stato in cui si è
     (cosa e come può scrivere, frasi del tipo "Scegli una mossa per il tuo turno:", ....)
     Pensare se ha più senso che questa funzione sia scritta qui o singolarmente sulle due View.
     Eventualmente, tutte le funzioni draw...() saranno chiamate sulla View (gui o cli che sia).
     */
    public void printMoveLegend(){

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
                //view.drawConfigurationChoice("Single");
                break;
            case MP_CONFIGURATION_CHOOSE:
                //view.drawConfigurationChoice("Multi");
                break;
            case CONFIGURATION:
                //view.drawConfigurationLayout(int configPiece);
                break;

           //TODO: finish cases
        }

    }


    public boolean isAccepted(CtoSMessageType type){
        return acceptedMessagesMap.get(currentState).contains(type);
    }

    public void setLightMatch(Summary summary){
        LightMatch match = new LightMatch(summary);
        match.setView(this.view);
    }

}
