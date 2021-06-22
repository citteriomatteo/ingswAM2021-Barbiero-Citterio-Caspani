package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.model.match.player.Player;

import java.util.List;

/**
 * This interface is used to let other classes access only to the player's list from a match and nothing else.
 */
public interface Communicator {

    /**
     * This method returns the list of players
     * @return the list of players
     */
    List<Player> getPlayers();
}
