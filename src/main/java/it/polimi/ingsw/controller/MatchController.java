package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.*;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.EndGameResultsMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.PlayerConnectionStateMessage;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.*;

import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static java.util.Map.entry;
import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;


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
    public Player getCurrentPlayer(){
        return match.getCurrentPlayer();
    }

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

    public Map<String, Card> getCardMap(){
        return cardMap;
    }

    public Match getMatch(){
        return match;
    }


    //%%%%%%%%%%%%%%%%%% SETTER %%%%%%%%%%%%%%%%%%%

    public void setState(String nickname, StateName state){
        if(state.getVal()<4)
            startingPhaseController.setState(nickname, state);
        else if(state.getVal()<12){
            if (turnController == null)
                turnController = new TurnController(match, cardMap);
            turnController.setCurrentState(state);
        }
    }

    public void setWhiteMarblesDrawn(int num){ turnController.setWhiteMarbleDrawn(num);}
    public void setLastRound(boolean lastRound) { turnController.setLastRound(lastRound);}

    public Map<String, Card> initCardMap(MatchConfiguration configuration){
        Map<String, Card> map = new HashMap<>();
        for (int i=1; i<=configuration.getAllDevCards().size(); i++)
            map.put("D"+i,configuration.getAllDevCards().get(i-1));
        for (int i=1; i<=configuration.getAllLeaderCards().size(); i++)
            map.put("L"+i,configuration.getAllLeaderCards().get(i-1));

        return map;
    }

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

    public synchronized boolean startingLeader(String nickname, List<String> leaders) throws RetryException {
        boolean isComputable = isComputable(nickname, LEADERS_CHOICE);
        if (!isComputable)
            return false;

        lastUsedState = startingPhaseController.leadersChoice(nickname, leaders);

        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;

    }

    public synchronized boolean startingResources(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.STARTING_RESOURCES);
        if (!isComputable)
            return false;

        lastUsedState = startingPhaseController.startingResources(nickname, resources);

        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;
    }

    public synchronized  boolean disconnection(Player player){
        if(turnController!=null && player.equals(match.getCurrentPlayer())){
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

    public synchronized boolean switchShelf(String nickname, int shelf1, int shelf2) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.SWITCH_SHELF);
        if (!isComputable)
            return false;

        turnController.switchShelf(shelf1, shelf2);

        return true;

    }

    public synchronized boolean leaderActivation(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_ACTIVATION);
        if (!isComputable)
            return false;

        turnController.leaderActivation(leaderId);

        return true;
    }

    public synchronized boolean leaderDiscarding(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_DISCARDING);
        if (!isComputable)
            return false;

        turnController.leaderDiscarding(leaderId);

        return true;
    }

    public synchronized boolean marketDraw(String nickname, boolean row, int num) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.MARKET_DRAW);
        if (!isComputable)
            return false;

        lastUsedState = turnController.marketDraw(row, num);

        return true;
    }

    public synchronized boolean whiteMarblesConversion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WHITE_MARBLE_CONVERSIONS);
        if (!isComputable)
            return false;

        lastUsedState = turnController.whiteMarblesConversion(resources);

        return true;
    }

    public synchronized boolean warehouseInsertion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WAREHOUSE_INSERTION);
        if (!isComputable)
            return false;

        lastUsedState = turnController.warehouseInsertion(resources);

        return true;
    }

    public synchronized boolean devCardDraw(String nickname, int row, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_DRAW);
        if (!isComputable)
            return false;

        lastUsedState = turnController.devCardDraw(row, column);

        return true;
    }

    public synchronized boolean payments(String nickname, List<PhysicalResource> strongboxCosts, Map<Integer, PhysicalResource> warehouseCosts) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PAYMENTS);
        if (!isComputable)
            return false;

        lastUsedState = turnController.payments(strongboxCosts, warehouseCosts);

        return true;
    }

    public synchronized boolean devCardPlacement(String nickname, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_PLACEMENT);
        if (!isComputable)
            return false;

        lastUsedState = turnController.devCardPlacement(column);

        return true;
    }

    public synchronized boolean production(String nickname, List<String> cardIds, Production productionOfUnknown) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PRODUCTION);
        if (!isComputable)
            return false;

        lastUsedState = turnController.production(cardIds, productionOfUnknown);

        return true;
    }

    private void matchEndingProcedure(MatchEndedException e){
        rematchPhaseController = new RematchPhaseController(match.getPlayers());

        lastUsedState = StateName.END_MATCH;
        for(Player p : match.getPlayers())
            new NextStateMessage(p.getNickname(), lastUsedState).send(p.getNickname());

        EndGameResultsMessage message = new EndGameResultsMessage("", e.getMessage(), e.getRanking());
        message.sendBroadcast(match);
    }

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
            } catch (SingleMatchException e) { e.printStackTrace(); System.exit(1); }
            catch (WrongSettingException e) { e.printStackTrace(); System.exit(1); }

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
