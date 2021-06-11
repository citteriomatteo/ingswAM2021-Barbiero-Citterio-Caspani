package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.*;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.EndGameResultsMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.PlayerConnectionStateMessage;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.*;

import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static java.util.Map.entry;
import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;


/**
 * This class works as a proxy controller, delegating the job to the various other controllers.
 * It also controls the computability of messages from clients, before calling the controllers.
 */

public class MatchController {
    private Match match;
    private TurnController turnController;
    private StartingPhaseController startingPhaseController;
    private RematchPhaseController rematchPhaseController;
    private StateName lastUsedState;
    private final Map<String, Card> cardMap;
    private static final Map<StateName, List<CtoSMessageType>> acceptedMessagesMap;
    static {
        acceptedMessagesMap = Map.ofEntries(
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

    /**
     * Standard configuration constructor: only players passed to the controller.
     * @param playersInMatch the players
     */
    public MatchController(List<Player> playersInMatch) {
        MatchConfiguration configuration = assignConfiguration("src/main/resources/json/StandardConfiguration.json");
        cardMap = initCardMap(configuration);

        //setting the starting summary to everyone
        ModelObserver obs = new Summary(playersInMatch, cardMap, configuration.getCustomPath(), configuration.getBasicProduction());
        for(Player p : playersInMatch)
            p.setSummary(obs);

        try {
            this.match = new MultiMatch(playersInMatch, configuration);
        } catch (SingleMatchException e) {
            System.err.println("internal error");
            System.exit(1);
        } catch (WrongSettingException e) {
            System.exit(1);
        }

        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

    }

    /**
     * Constructor that's specific for a custom configuration.
     * @param playersInMatch the players
     * @param configuration  the custom configuration
     * @throws RetryException
     */
    public MatchController(List<Player> playersInMatch, MatchConfiguration configuration) throws RetryException {
        cardMap = initCardMap(configuration);

        //setting the starting summary to everyone
        try {
            this.match = new MultiMatch(playersInMatch, configuration);
        } catch (SingleMatchException e) {
            System.err.println("internal error");
            System.exit(1);
        } catch (WrongSettingException e) {
            throw new RetryException("Wrong configuration");
        }

        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

    }

    /**
     * Specific constructor for single player matches with the standard configuration.
     * @param player the only player in match
     */
    public MatchController(Player player) {
        MatchConfiguration configuration = MatchConfiguration.assignConfiguration("src/main/resources/json/StandardConfiguration.json");
        cardMap = initCardMap(configuration);

        //setting the starting summary to the player
        ModelObserver obs = new Summary(new ArrayList<>(List.of(player)), cardMap, configuration.getCustomPath(), configuration.getBasicProduction());
        player.setSummary(obs);

        try {
            this.match = new SingleMatch(player, configuration);
        } catch (WrongSettingException e) {
            System.exit(1);
        }

        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

    }

    /**
     * Specific constructor for single player matches with a custom configuration.
     * @param player the only player in match
     */
    public MatchController(Player player, MatchConfiguration configuration) throws RetryException {
        cardMap = initCardMap(configuration);

        //setting the starting summary to the player
        ModelObserver obs = new Summary(new ArrayList<>(List.of(player)), cardMap, configuration.getCustomPath(), configuration.getBasicProduction());
        player.setSummary(obs);

        try {
            this.match = new SingleMatch(player, configuration);
        } catch (WrongSettingException e) {
            throw new RetryException("Wrong configuration");
        }

        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

    }

    //%%%%%%%%%%%%%%%%%% GETTER %%%%%%%%%%%%%%%%%%%

    /**
     * Getter for the current player.
     * @return the current player
     */
    public Player getCurrentPlayer(){
        return match.getCurrentPlayer();
    }

    /**
     * Getter for the current state of the player which nickname is passed as a parameter.
     * @param nickname the nickname
     * @return         a StateName
     */
    public StateName getCurrentState(String nickname) {
        if (turnController == null)
            return startingPhaseController.getPlayerState(nickname);
        if (rematchPhaseController != null)
            return StateName.REMATCH_OFFER;
        else{
            if (nickname.equals(turnController.getCurrentPlayer().getNickname()))
                return turnController.getCurrentState();
            else
                return StateName.WAITING_FOR_TURN;
        }
    }

    /**
     * Getter for the map of cards
     * @return the cards map
     */
    public Map<String, Card> getCardMap(){
        return cardMap;
    }

    /**
     * Getter for the match instance
     * @return the match
     */
    public Match getMatch(){
        return match;
    }


    //%%%%%%%%%%%%%%%%%% SETTER %%%%%%%%%%%%%%%%%%%

    /**
     * This method sets the game state of the player with this nickname to the state passed.
     * @param nickname the player
     * @param state    its state
     */
    public void setState(String nickname, StateName state){
        if(state.getVal()<4)
            startingPhaseController.setState(nickname, state);
        else if(state.getVal()<12){
            if (turnController == null)
                turnController = new TurnController(match, cardMap);
            turnController.setCurrentState(state);
        }
    }

    /**
     * This method sets the number of white marbles drawn during the last market draw.
     * @param num the number
     */
    public void setWhiteMarblesDrawn(int num){ turnController.setWhiteMarbleDrawn(num);}

    /**
     * This method eventually raises the last round flag.
     * @param lastRound the value of the flag
     */
    public void setLastRound(boolean lastRound) { turnController.setLastRound(lastRound);}

    /**
     * This method initializes the card map with the cards in the passed configuration.
     * @param configuration the configuration
     * @return the filled map
     */
    public Map<String, Card> initCardMap(MatchConfiguration configuration){
        Map<String, Card> map = new HashMap<>();
        for (int i=1; i<=configuration.getAllDevCards().size(); i++)
            map.put("D"+i,configuration.getAllDevCards().get(i-1));
        for (int i=1; i<=configuration.getAllLeaderCards().size(); i++)
            map.put("L"+i,configuration.getAllLeaderCards().get(i-1));

        return map;
    }

    /**
     * This method calculates if the arrived message is valid for the sender player's state.
     * @param nickname the sender
     * @param type     the type of the message
     * @return         the result
     * @throws RetryException in case the message is not accepted basing on his current state.
     */
    private boolean isComputable(String nickname, CtoSMessageType type) throws RetryException {
        if (turnController == null) {
            if(match.getPlayer(nickname) == null)
                return false;
        if (!acceptedMessagesMap.get(startingPhaseController.getPlayerState(nickname)).contains(type))
                throw new RetryException("This message is not accepted in this phase");
        } else if (rematchPhaseController == null) {
            if (!nickname.equals(turnController.getCurrentPlayer().getNickname()))
                throw new RetryException("It's not your turn, please wait");
            else if (!acceptedMessagesMap.get(turnController.getCurrentState()).contains(type))
                throw new RetryException("This message is not accepted in this phase");
        } else if (!type.equals(CtoSMessageType.REMATCH))
            throw new RetryException("This message is not accepted in this phase");


        return true;
    }

    /**
     * This method accepts the starting leaders' choices and sends the player
     * to the StartingResource or StartingPhaseDone state.
     * @param nickname the player
     * @param leaders  its leaders
     * @return a boolean
     * @throws RetryException if isComputable goes wrong
     */
    public synchronized boolean startingLeader(String nickname, List<String> leaders) throws RetryException {
        boolean isComputable = isComputable(nickname, LEADERS_CHOICE);
        if (!isComputable)
            return false;

        lastUsedState = startingPhaseController.leadersChoice(nickname, leaders);

        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;

    }

    /**
     * This method accepts the starting resources' choices and proceeds with the player's state.
     * @param nickname the player
     * @param resources the resources chosen
     * @return a boolean
     * @throws RetryException if isComputable goes wrong.
     */
    public synchronized boolean startingResources(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.STARTING_RESOURCES);
        if (!isComputable)
            return false;

        lastUsedState = startingPhaseController.startingResources(nickname, resources);

        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;
    }

    /**
     * This method handles the player's disconnection and proceeds going ahead with the turns.
     * @param player the disconnected one
     * @return a boolean
     */
    public synchronized  boolean disconnection(Player player){
        if(turnController!=null && player.equals(match.getCurrentPlayer()) && match.getPlayers().size() != 1){
            try {
                turnController.nextTurn();
            } catch (MatchEndedException e) {
                matchEndingProcedure(e);
            }
        }

        //update_call (useful information for light match)
        player.getSummary().updateConnectionState(player.getNickname(), false);

        PlayerConnectionStateMessage message = new PlayerConnectionStateMessage(player.getNickname(), false);
        message.sendBroadcast(match);

        return true;
    }

    /**
     * This method checks if the player is in end turn's phase and, eventually, passes to the next player's turn.
     * @param nickname the player that finished a turn
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean nextTurn(String nickname) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.END_TURN);
        if (!isComputable)
            return false;
        try {
            turnController.nextTurn();
        }
        catch (MatchEndedException e) {
            matchEndingProcedure(e);
        }

        return true;
    }

    /**
     * This method accepts switch shelves' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param shelf1 the first shelf
     * @param shelf2 the second shelf
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean switchShelf(String nickname, int shelf1, int shelf2) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.SWITCH_SHELF);
        if (!isComputable)
            return false;

        turnController.switchShelf(shelf1, shelf2);

        return true;

    }

    /**
     * This method accepts leader activation's messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param leaderId the chosen leader
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean leaderActivation(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_ACTIVATION);
        if (!isComputable)
            return false;

        turnController.leaderActivation(leaderId);

        return true;
    }

    /**
     * This method accepts leader discarding messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param leaderId the chosen leader
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean leaderDiscarding(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_DISCARDING);
        if (!isComputable)
            return false;

        turnController.leaderDiscarding(leaderId);

        return true;
    }

    /**
     * This method accepts market draws' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param row row/column
     * @param num the number of row/column
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean marketDraw(String nickname, boolean row, int num) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.MARKET_DRAW);
        if (!isComputable)
            return false;

        lastUsedState = turnController.marketDraw(row, num);

        return true;
    }

    /**
     * This method accepts conversions' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param resources the resources converted
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean whiteMarblesConversion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WHITE_MARBLE_CONVERSIONS);
        if (!isComputable)
            return false;

        lastUsedState = turnController.whiteMarblesConversion(resources);

        return true;
    }

    /**
     * This method accepts warehouse insertions' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param resources the resources, with the quantity = shelf.
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean warehouseInsertion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WAREHOUSE_INSERTION);
        if (!isComputable)
            return false;

        lastUsedState = turnController.warehouseInsertion(resources);

        return true;
    }

    /**
     * This method accepts dev card draws' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param row the row
     * @param column the column
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean devCardDraw(String nickname, int row, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_DRAW);
        if (!isComputable)
            return false;

        lastUsedState = turnController.devCardDraw(row, column);

        return true;
    }

    /**
     * This method accepts payments' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param strongboxCosts the costs from sb
     * @param warehouseCosts the costs from wh
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean payments(String nickname, List<PhysicalResource> strongboxCosts, Map<Integer, PhysicalResource> warehouseCosts) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PAYMENTS);
        if (!isComputable)
            return false;

        lastUsedState = turnController.payments(strongboxCosts, warehouseCosts);

        return true;
    }

    /**
     * This method accepts dev card placement's messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param column the slot
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean devCardPlacement(String nickname, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_PLACEMENT);
        if (!isComputable)
            return false;

        lastUsedState = turnController.devCardPlacement(column);

        return true;
    }

    /**
     * This method accepts productions' messages from the clients and delegates the operation to the turnController.
     * @param nickname the sender
     * @param cardIds the ids of cards produced
     * @param productionOfUnknown the production of all unknowns
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean production(String nickname, List<String> cardIds, Production productionOfUnknown) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PRODUCTION);
        if (!isComputable)
            return false;

        lastUsedState = turnController.production(cardIds, productionOfUnknown);

        return true;
    }

    /**
     * This method handles the whole end match procedure, sending results to the player and initializing the last controller.
     * It inserts "Lorenzo the Magnificent" in first or last position, in case the match is single player.
     * @param e the exception
     */
    private void matchEndingProcedure(MatchEndedException e){
        rematchPhaseController = new RematchPhaseController(match.getPlayers());

        lastUsedState = StateName.END_MATCH;
        for(Player p : match.getPlayers())
            new NextStateMessage(p.getNickname(), lastUsedState).send(p.getNickname());

        //adding Lorenzo to the ranking
        if(match.getPlayers().size() == 1) {

            boolean lorenzoWon = match.getCardGrid().emptyColumnExists();
            if(( (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath() ).getBlackPosition() == 24)
                lorenzoWon = true;

            e.getRanking().put("Lorenzo \nthe Magnificent", (lorenzoWon ? 999 : -1));
            System.out.println("putting Lorenzo to " + (lorenzoWon ? 999 : -1) + "points in the ranking");


        }

        EndGameResultsMessage message = new EndGameResultsMessage("", e.getMessage(), e.getRanking());
        message.sendBroadcast(match);
    }

    /**
     * This method accepts rematch response's messages from the clients and delegates the operation to the rematchController.
     * @param nickname the sender
     * @param value the acceptation/decline
     * @return a boolean
     * @throws RetryException from isComputable
     */
    public synchronized boolean response(String nickname, boolean value) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.REMATCH);
        if (!isComputable)
            return false;

        try {
            rematchPhaseController.response(nickname, value);
        } catch (MatchRestartException e) {
            restartMatch();
        }
        return true;
    }

    /**
     * This method is called when every player in match has accepted the rematch: it re-initializes controllers, the summary and
     * clears players' content back to start.
     */
    private void restartMatch(){
        List<Player> oldPlayers = match.getPlayers();
        MatchConfiguration oldConfiguration = match.getMatchConfiguration();

        //re-setting the starting summary to everyone
        ModelObserver obs = new Summary(oldPlayers, cardMap, oldConfiguration.getCustomPath(), oldConfiguration.getBasicProduction());
        for(Player p : oldPlayers) {
            p.clear();
            p.setSummary(obs);
        }

        if(match.getPlayers().size() == 1)
            try {
                match = new SingleMatch(match.getPlayers().get(0), match.getMatchConfiguration());
            } catch (WrongSettingException e) { e.printStackTrace(); System.exit(1); }

        else
            try {
                match = new MultiMatch(match.getPlayers(), match.getMatchConfiguration());
            } catch (SingleMatchException | WrongSettingException e) { e.printStackTrace(); System.exit(1); }

        //re-initializing controllers for a new match.
        turnController = null;
        rematchPhaseController = null;
        startingPhaseController = new StartingPhaseController(match, cardMap);


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
