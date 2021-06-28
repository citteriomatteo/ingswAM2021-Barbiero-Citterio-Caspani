package it.polimi.ingsw.view;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.model.essentials.Card;
import it.polimi.ingsw.gameLogic.model.match.Summary;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.ctosmessage.RematchMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.lightmodel.LightMatch;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.network.client.LocalClient.getLocalClient;
import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static java.util.Map.entry;

/**
 * This class implements a Singleton controller for the client.
 * This controller's functionalities are shared by every class that implements View.
 * Its main task is to handle the flow of game messages, including principally retry and next state messages,
 * and check if a certain message coming from the client is able to be accepted from the server (only in terms of
 * current state).
 */
public class ClientController
{
    static private final ClientController instance = new ClientController();
    private LightMatch match;
    private View view;     // Controller has a View reference for managing retry and next state directly
    private StateName currentState;
    private String nickname;
    private boolean local = false;
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
                        MARKET_DRAW, DEV_CARD_DRAW, PRODUCTION, ADD_FAITH)),
                entry(StateName.MARKET_ACTION, List.of(WHITE_MARBLE_CONVERSIONS, SWITCH_SHELF)),
                entry(StateName.RESOURCES_PLACEMENT, List.of(WAREHOUSE_INSERTION, SWITCH_SHELF)),
                entry(StateName.BUY_DEV_ACTION, List.of(SWITCH_SHELF, PAYMENTS)),
                entry(StateName.PLACE_DEV_CARD, List.of(SWITCH_SHELF, DEV_CARD_PLACEMENT)),
                entry(StateName.PRODUCTION_ACTION, List.of(SWITCH_SHELF, PAYMENTS)),
                entry(StateName.END_TURN, List.of(SWITCH_SHELF, LEADER_ACTIVATION, LEADER_DISCARDING, END_TURN, ADD_FAITH)),
                entry(StateName.END_MATCH, List.of(REMATCH, DISCONNECTION)),
                entry(StateName.REMATCH_OFFER, List.of(REMATCH, DISCONNECTION))
        );
    }

    /**
     * Constructor: sets the player to the LOGIN state, ready to start.
     */
    private ClientController(){
        currentState = StateName.LOGIN;
    }


    /**
     * Static getter method.
     * @return the Singleton instance of the ClientController
     */
    public static ClientController getClientController(){
        return instance;
    }

    /**
     * Called when the match is played locally.
     * Sets the variable 'local' as true and sets the corresponding variable in Message.
     */
    public void setIsLocal(){
        if(!local)
            this.local = true;

        Message.setIsLocal();
    }

    /**
     * Setter of the view (in order to call update/layout methods on it).
     * @param view the cli/gui view
     */
    public void setView(View view){
        this.view = view;
        view.printTitle();
    }

    /**
     * Getter
     * @return true if this match is played locally, false elsewhere.
     */
    public boolean isLocal(){
        return local;
    }

    /**
     * Processes retry/next states messages:
     * - for retries: prints an error layout message after re-printing the current state layout message.
     * - for next states: prints the new state layout message and sets the lastRound flag to false at the beginning
     *                    of the starting phase (for a correct rematch reset).
     * @param msg the received message
     */
    public void updateCurrentState(StoCMessage msg){
        if(msg.getType().equals(StoCMessageType.RETRY)) {
            RetryMessage rMsg = (RetryMessage) msg;
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

    /**
     * Called after an internal (from the KeyboardReader) or external (after a server's retry message) retry call.
     * @param errMessage the error message
     * @param currentState the state of the player after the retry
     */
    public void printRetry(String errMessage, StateName currentState){
        view.printRetry(errMessage, currentState, match);
    }

    /**
     * Calls the specific view's ranking print method.
     * @param message an extra message
     * @param ranking the final non-sorted ranking
     */
    public void printMatchResults(String message, Map<String, Integer> ranking){
        view.printMatchResults(message, ranking);
    }

    /**
     * Calls the specific view's last round print method.
     */
    public void printLastRound(){
        view.printLastRound();
    }

    /**
     * Calls the specific view's token draw print method.
     * @param tokenName the name of the token
     * @param remainingTokens the number of tokens left on the stack
     */
    public void printTokenDraw(String tokenName, int remainingTokens){
        view.printTokenDraw(tokenName, remainingTokens);
    }

    /**
     * Calls a layout draw on the specific view depending on the player's current state.
     * @param msg passed for the REMATCH_OFFER state to print who offered a rematch
     */
    private void printMoveLegend(StoCMessage msg){

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
                view.drawWaitingLayout();
                view.drawStartingPhaseDone();
                break;
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

    /**
     * Controls if the message can be sent to the server and then sends it.
     * @param message the message to send
     * @return true if the message has been sent
     */
    public boolean send(CtoSMessage message){
        if(message.getType().equals(CtoSMessageType.BINARY_SELECTION) && currentState.equals(StateName.END_MATCH))
            message = convertIntoRematchMessage((BinarySelectionMessage) message);

        if(isAccepted(message.getType())) {
            if(local)
                return getLocalClient().writeMessage(message);
            else
                return getClient().writeMessage(message);
        }
        printRetry("You're in " + currentState + ". Operation not available: retry. Write 'help' for message tips.", currentState);
        return false;
    }

    /**
     * Converts a processed binary selection into a RematchMessage (for the last rematch phase).
     * @param message the binary selection message
     * @return the corresponding rematch message
     */
    private RematchMessage convertIntoRematchMessage(BinarySelectionMessage message){
        boolean selection = message.getSelection();
        return new RematchMessage(message.getNickname(), selection);
    }

    /**
     * Checks if the CtoSMessageType is correct basing on the current player's state.
     * @param type the type of the message
     * @return true if it's accepted to leave the client, false elsewhere
     */
    public boolean isAccepted(CtoSMessageType type){
        if (type == DISCONNECTION)
            return true;
        return acceptedMessagesMap.get(currentState).contains(type);
    }

    /**
     * Takes a Summary and sets the local LightMatch with it.
     * @param summary the summary coming from the SummaryMessage
     */
    public void setLightMatch(Summary summary){
        match = new LightMatch(summary, this.view);
    }

    /**
     * Takes a string request, elaborates it and extracts the IDs of the cards to print.
     * Then calls the drawCards method on the view to print them properly.
     * @param request the string to elaborate
     */
    public void printCardInfo(String request){
        List<String> splitRequest = new ArrayList(Arrays.asList(request.split("\\s+")));
        splitRequest.remove(0);
        if(splitRequest.size() != 1)
            return;
        List<String> cards = new ArrayList(Arrays.asList(splitRequest.get(0).split(",")));
        for(int i = 0; i < cards.size(); i++) {
            Card chosen = LightMatch.getCardMap().get(cards.get(i).toUpperCase());
            if(chosen == null){
                System.out.println("Card " + cards.get(i).toUpperCase() + " does not exist.");
                cards.remove(cards.get(i));
            }
        }
        view.drawCards(cards.stream().map(String::toUpperCase).collect(Collectors.toList()));

    }

    /**
     * Calls a "rematch offered" layout print on the view.
     * @param message the nickname of the proposer
     */
    public void printRematchOffer(String message){
        view.drawRematchOfferLayout(message);
    }

    /**
     * Receives a goodbye message from the server and calls the layout method on the view to print it properly.
     * @param msg the goodbye message
     */
    public void printGoodbyeMessage(String msg) {
        view.drawGoodbyeLayout(msg);
    }

    /**
     * Receives a "discount map" print request from the client and calls the print method on the CLI view to print it properly.
     * @param nickname the nickname of the requester
     */
    public void printDiscountMap(String nickname){
        view.printDiscountMap(match.getLightPlayer(nickname));
    }
    /**
     * Receives a "white marble conversions" print request from the client and calls the print method
     * on the CLI view to print it properly.
     * @param nickname the nickname of the requester
     */
    public void printWhiteMarbleConversions(String nickname){
        view.printWhiteMarbleConversions(match.getLightPlayer(nickname));
    }

    //&&&&&&&&&&&&&&&&& GETTERS AND SETTERS &&&&&&&&&&&&&&&&&
    /**
     * Getter
     * @return the light match
     */
    public LightMatch getMatch(){ return match; }
    /**
     * Getter
     * @return the current state
     */
    public StateName getCurrentState() { return currentState; }
    /**
     * Getter
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Setter
     * @param nickname the nickname to set to the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    /**
     * Getter
     * @return the view instance
     */
    public View getView(){
        return view;
    }
}
