package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
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
                entry(StateName.CONFIGURATION, CONFIGURE)
        );
    }
    private StateName currentState;
    private final PlayerHandler client;
    private String playerNickname;

    public InitController(PlayerHandler client) {
        currentState = StateName.LOGIN;
        this.client = client;
    }

    public boolean login(String nickname){
        if(!isAccepted(LOGIN))
            return false;
        //todo: controls on existing nicknames

        client.setPlayer(new Player(nickname));
        playerNickname = nickname;
        addNewPlayer(client);
        currentState = StateName.NEW_PLAYER;
        (new NextStateMessage(playerNickname, currentState)).send(playerNickname);
        return true;
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
                    if (isThereAPendentMatch()) {
                        changeState(StateName.WAITING);
                        participateToCurrentMatch(client.getPlayer());
                    }else
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
                        client.setMatchController(new MatchController(playersInMatch));  //<-- exit from init
                    } catch (RetryException e) {
                        //TODO: wrong match choose
                        e.printStackTrace();
                    }
                }
                else
                    changeState(StateName.CONFIGURATION);

            case RECONNECTION:
                //todo
                return true;

            default: //if the method enters here there is a problem
                return false;
        }
    }

    public boolean isAccepted(CtoSMessageType msg){
        return msg.equals(acceptedMessageMap.get(currentState));
    }

    private void changeState(StateName nextState){
        currentState = nextState;
        (new NextStateMessage(playerNickname, currentState)).send(playerNickname);
    }
}
