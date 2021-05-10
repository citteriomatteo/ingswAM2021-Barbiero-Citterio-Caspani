package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.ReconnectionException;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
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

    public InitController(PlayerHandler client) {
        currentState = StateName.LOGIN;
        this.client = client;
        client.write(new NextStateMessage(null, currentState));
    }

    public boolean login(String nickname){
        if(!isAccepted(LOGIN))
            return false;

        try {
            if(!addNewPlayer(nickname, client)) {
                client.write(new RetryMessage(null, "This name is used by another connected player, please choose a different one"));
                client.write(new NextStateMessage(null, currentState));
                return true;
            }
            client.setPlayer(new Player(nickname));
            playerNickname = nickname;
            changeState(StateName.NEW_PLAYER);
            return true;

        } catch (ReconnectionException e) {
            changeState(StateName.RECONNECTION);
            return true;
        }

    }

    public boolean selection(boolean choice){
        if(!isAccepted(BINARY_SELECTION))
            return false;
        switch (currentState){
            case NEW_PLAYER:
                if(choice) {
                    //single player
                    System.out.println("Player: " + playerNickname + " chose to play a singlePlayer match");
                    changeState(StateName.SP_CONFIGURATION_CHOOSE);
                }
                else {
                    // multiplayer
                    System.out.println("Player: " + playerNickname + " chose to play a multiplayer match");
                    if (isThereAPendentMatch()) { //Found a pendent match, try to add the player to it
                        changeState(StateName.WAITING);
                        if(participateToCurrentMatch(client.getPlayer())) {//If the player is added to the game <-- exit from init
                            changeState(StateName.START_GAME);
                            return true;
                        }
                        //Failed adding the player, the player will have to make his own game
                        changeState(StateName.NUMBER_OF_PLAYERS);
                        return true;

                    }else
                        //He is the first player of the game, he has to choose the number of players
                        changeState(StateName.NUMBER_OF_PLAYERS);
                }
                return true;

            case SP_CONFIGURATION_CHOOSE:
                if(choice) //choose the default config
                    client.setMatchController(new MatchController(client.getPlayer()));  //<-- exit from init
                else
                    changeState(StateName.CONFIGURATION);
                return true;

            case MP_CONFIGURATION_CHOOSE:
                if(choice) { //choose the default config, wait for players and then start the match
                    List<Player> playersInMatch = matchParticipants();
                    System.out.println("forming a new match for... " + playersInMatch);
                    try {
                        System.out.println("------------------------> enter the constructor of Match controller");
                        MatchController controller = new MatchController(playersInMatch);
                        System.out.println("------------------------> exit the constructor of match controller");
                        setMatchController(controller, playersInMatch);  //<-- exit from init
                    } catch (RetryException e) {
                        //TODO: wrong configuration
                        e.printStackTrace();
                    }
                    changeState(StateName.START_GAME);
                }
                else { //Choose custom configuration
                    changeState(StateName.CONFIGURATION);
                }
                return true;

            case RECONNECTION:
                //todo reconnection
                return true;

            default: //if the method enters here there is a problem
                return false;
        }
    }

    public boolean setNumberOfPlayers(int num){
        if(!isAccepted(NUM_PLAYERS))
            return false;
        searchingForPlayers(client.getPlayer(), num);
        System.out.println(client.getPlayer() + " is searching for " + num + " players...");
        changeState(StateName.MP_CONFIGURATION_CHOOSE);
        return true;
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
            System.out.println("Try to set the controller for " + player);
            findControlBase(player.getNickname()).setMatchController(controller);
        }
    }
}
