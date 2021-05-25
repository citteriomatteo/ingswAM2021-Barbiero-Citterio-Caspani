package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.MatchRestartException;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.stocmessage.GoodbyeMessage;
import it.polimi.ingsw.network.message.stocmessage.RematchOfferedMessage;

import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.server.ServerUtilities.serverCall;

public class RematchPhaseController
{
    private int numResponses=0;
    private List<Player> players;
    private List<String> pendantPlayers;


    public RematchPhaseController(List<Player> players){
        this.players = players;
        for(Player p : players)
            pendantPlayers.add(p.getNickname());
    }

    public boolean response(String nickname, boolean value) throws MatchRestartException, RetryException {

        if(!pendantPlayers.contains(nickname))
            throw new RetryException("Wait for the other players to make a decision.");

        pendantPlayers.remove(nickname);

        if(!value){
            new GoodbyeMessage(nickname, "One player declined the rematch offer. Thanks for playing!", false).sendBroadcast(players.get(0).getMatch());
            for(Player player : players)
                serverCall().findControlBase(player.getNickname()).endGame();
            return false;
        }
        if(numResponses==0) {
            numResponses++;
            new RematchOfferedMessage("", nickname).sendBroadcast(pendantPlayers);
            return true;
        }
        if(numResponses==players.size()-1){
            numResponses++;
            throw new MatchRestartException("Restart the match.");
        }
        numResponses++;
        return false;
    }


}
