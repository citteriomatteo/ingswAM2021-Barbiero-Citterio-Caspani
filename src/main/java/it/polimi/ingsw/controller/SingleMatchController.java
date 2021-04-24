package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.match.player.Player;

public class SingleMatchController extends Thread implements MatchController {
    private Player player;

    public SingleMatchController(Player player){
        this.player = player;
    }

    @Override
    public void run() {

    }
}
