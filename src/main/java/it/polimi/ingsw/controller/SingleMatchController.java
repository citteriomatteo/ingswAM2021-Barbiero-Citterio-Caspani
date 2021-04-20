package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.match.player.Player;

public class SingleMatchController extends Thread {

    private Player player;
    public SingleMatchController(String nickname){
        player = new Player(nickname);
    }
    @Override
    public void run() {

    }
}
