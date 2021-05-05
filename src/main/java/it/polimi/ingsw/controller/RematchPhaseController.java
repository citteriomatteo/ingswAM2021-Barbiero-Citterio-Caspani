package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.match.player.Player;

import java.util.List;

public class RematchPhaseController
{
    private int numResponses=0;
    private List<Player> players;

    public RematchPhaseController(List<Player> players){
        this.players = players;
    }

    public boolean response(String nickname, boolean value){
        if(!value){
            //TODO: broadcast the end of the game and disconnections
            //TODO: close everything
        }
        if(numResponses==0) {
            numResponses++;
            //TODO: broadcast the rematch offered by the player "nickname"
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
