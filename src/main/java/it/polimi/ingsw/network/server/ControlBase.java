package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.InitController;
import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;

/**
 * Interface used for communication with a specific player connected to the server
 */
public interface ControlBase {
    /** @return the current state of the player, either if he is in the first phases of connection or inside a match */
    StateName getCurrentState();
    /** @return the MatchController related to this */
    MatchController getMatchController();
    /** @return the InitController related to this */
    InitController getInitController();
    /** @return the player related to this client */
    Player getPlayer();
    /** @return the nickname of the player related to this. If there isn't yet a related player returns null. */
    String getNickname();

    /**
     * Sets the player related to this client
     * @param player the player related to this client
     */
    void setPlayer(Player player);

    /**
     * Links the match controller to this client, when this method is called
     * the client is considered in the match and so he will accept different types of messages
     * @param controller the matchController to link
     */
    void setMatchController(MatchController controller);

    /**
     * Method called at the end of the match -> if this method is called the match has comes naturally to
     * an end and the player can be disconnected without issues
     */
    void endGame();

    /**
     * Writes something to this player, the message has to be written in one of the subclasses of {@link StoCMessage}
     * @param msg the message you want to send to this player
     * @return true if the message has been sent, false if something goes wrong in the output stream
     */
    boolean write(StoCMessage msg);
}
