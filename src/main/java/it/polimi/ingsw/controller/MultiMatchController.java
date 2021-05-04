package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;

import java.util.List;


public class MultiMatchController implements MatchController {
    private MultiMatch match;

    public MultiMatchController(List<Player> playersInMatch) throws SingleMatchException {
    }

}
