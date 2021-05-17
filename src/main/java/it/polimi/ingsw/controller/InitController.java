package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.ReconnectionException;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.message.stocmessage.SummaryMessage;
import it.polimi.ingsw.network.server.PlayerHandler;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType.*;
import static it.polimi.ingsw.network.server.ServerUtilities.*;
import static java.util.Map.entry;

public class InitController {
    private static final Map<StateName, CtoSMessageType> acceptedMessageMap;
    static {
        acceptedMessageMap = Map.ofEntries(
                entry(StateName.LOGIN, LOGIN),
                entry(StateName.RECONNECTION, BINARY_SELECTION),
                entry(StateName.NEW_PLAYER, BINARY_SELECTION),
                entry(StateName.NUMBER_OF_PLAYERS, NUM_PLAYERS),
                entry(StateName.SP_CONFIGURATION_CHOOSE, BINARY_SELECTION),
                entry(StateName.MP_CONFIGURATION_CHOOSE, BINARY_SELECTION),
                entry(StateName.CONFIGURATION, CONFIGURE),
                entry(StateName.WAITING, PING),
                entry(StateName.START_GAME, PING)

        );
    }
    private StateName currentState;
    private final PlayerHandler client;
    private String playerNickname;
    private int desiredNumberOfPlayers;

    public InitController(PlayerHandler client) {
        currentState = StateName.LOGIN;
        this.client = client;
        client.write(new NextStateMessage(null, currentState));

    }

    public StateName getCurrentState() {
        return currentState;
    }

    public boolean login(String nickname){
        if(!isAccepted(LOGIN))
            return false;

        try {
            if(serverCall().addNewPlayer(nickname, client)) {
                client.setPlayer(new Player(nickname));
                playerNickname = nickname;
                changeState(StateName.NEW_PLAYER);
                return true;
            }
            client.write(new RetryMessage(null, currentState , "This name is used by another connected player, please choose a different one"));
            client.write(new NextStateMessage(null, currentState));
            return true;

        } catch (ReconnectionException e) {
            playerNickname = nickname;
            currentState = StateName.RECONNECTION;
            client.write(new NextStateMessage(null, currentState));
            return true;
        }

    }

    public boolean selection(boolean choice) {
        if(!isAccepted(BINARY_SELECTION))
            return false;
        switch (currentState){
            case NEW_PLAYER:
                if(choice) {
                    //single player
                    System.out.println("Player: " + playerNickname + " chose to play a singlePlayer match");
                    changeState(StateName.SP_CONFIGURATION_CHOOSE);
                    return true;
                }

                // multiplayer
                System.out.println("Player: " + playerNickname + " chose to play a multiplayer match");

                if (serverCall().canCreateMatch(client.getPlayer())) {
                    //He is the first player of the game, he has to choose the number of players
                    changeState(StateName.NUMBER_OF_PLAYERS);
                    return true;
                }
                //Found a pendent match, and already added the player to it or to a waitingList for the next game
                // wait until the waiting queue is full, the state will be updated automatically
                return true;

            case SP_CONFIGURATION_CHOOSE:
                if(choice) { //choose the default config
                    changeState(StateName.START_GAME);
                    client.setMatchController(new MatchController(client.getPlayer()));  //<-- exit from init
                }
                else
                    changeState(StateName.CONFIGURATION);
                return true;

            case MP_CONFIGURATION_CHOOSE:
                if(choice) { //choose the default config, wait for players or start directly the match
                    changeState(StateName.WAITING_FOR_PLAYERS);
                    serverCall().searchingForPlayers(client.getPlayer(), desiredNumberOfPlayers);
                }
                else //Choose custom configuration
                    changeState(StateName.CONFIGURATION);
                return true;

            case RECONNECTION:
                if(choice && serverCall().reconnection(client, playerNickname)) {
                    changeState(StateName.START_GAME);
                    (new SummaryMessage(playerNickname, (Summary) client.getPlayer().getSummary())).send(playerNickname);
                    //todo maybe add a nextState message
                }
                else{
                    currentState = StateName.LOGIN;
                    client.write(new NextStateMessage(null, currentState));
                }
                return true;

            default: //if the method enters here there is a problem
                return false;
        }
    }

    public boolean setNumberOfPlayers(int num){
        if(!isAccepted(NUM_PLAYERS))
            return false;
        System.out.println(client.getPlayer() + " is searching for " + num + " players...");
        //Save the desired number of players in a variable in order to use this information later
        desiredNumberOfPlayers = num;
        changeState(StateName.MP_CONFIGURATION_CHOOSE);
        return true;
    }

    /**
     * A player in waiting state receiving this command can now create his own match
     */
    public synchronized void createMatch(){
        if(currentState != StateName.WAITING) {
            System.err.println("Error inside initController -> " + playerNickname + " cannot create a match because he is currently in state: " + currentState);
            System.exit(1);
        }
        changeState(StateName.NUMBER_OF_PLAYERS);
    }

    public boolean startGame(){
        switch (currentState) {
            case WAITING:
                changeState(StateName.START_GAME);
                return true;

            case WAITING_FOR_PLAYERS:
                List<Player> playersInMatch = serverCall().matchParticipants();
                System.out.println("forming a new match for... " + playersInMatch);
                changeState(StateName.START_GAME);
                MatchController controller = new MatchController(playersInMatch);
                setMatchController(controller, playersInMatch);  //<-- exit from init
                return true;

        }
        return false;
    }

    public void setWaiting(){
        changeState(StateName.WAITING);
    }

    public boolean isAccepted(CtoSMessageType msg){
        return msg.equals(acceptedMessageMap.get(currentState));
    }

    private void changeState(StateName nextState){
        currentState = nextState;
        (new NextStateMessage(playerNickname, currentState)).send(playerNickname);
    }

    private void setMatchController(MatchController controller, List<Player> playersInMatch){
        for(Player player : playersInMatch) {
            serverCall().findControlBase(player.getNickname()).setMatchController(controller);
        }
    }
}
