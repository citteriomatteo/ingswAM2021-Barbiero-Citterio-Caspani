package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.RematchMessage;
import it.polimi.ingsw.network.message.stocmessage.GoodbyeMessage;
import it.polimi.ingsw.network.message.stocmessage.RematchOfferedMessage;

import java.util.List;

import static it.polimi.ingsw.network.server.ServerUtilities.findControlBase;

public class RematchPhaseController
{
    private int numResponses=0;
    private List<Player> players;

    public RematchPhaseController(List<Player> players){
        this.players = players;
    }

    public boolean response(String nickname, boolean value){
        if(!value){
            new GoodbyeMessage(nickname, "One player declined the rematch offer. Thanks!").sendBroadcast(players.get(0).getMatch());
            //TODO: close everything
            return false;
        }
        if(numResponses==0) {
            numResponses++;
            new RematchOfferedMessage(nickname, "Rematch offered").sendBroadcast(players.get(0).getMatch());
            return true;
        }
        if(numResponses==players.size()-1){
            numResponses++;
            //TODO: throw an exception that determines the restart of the match
            return true;
        }
        numResponses++;
        return false;
    }


}
