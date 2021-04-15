package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.match.player.Player;

import java.util.List;

public interface Comunicator {

    /**
     * This method returns the list of players
     * @return the list of players
     */
    List<Player> getPlayers();
}
