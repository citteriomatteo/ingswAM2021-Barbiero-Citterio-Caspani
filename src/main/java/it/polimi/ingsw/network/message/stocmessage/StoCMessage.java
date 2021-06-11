package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;

import static it.polimi.ingsw.network.server.ServerUtilities.serverCall;

/**
 * This abstract class implements the common structure of every server-to-client message.
 */
public abstract class StoCMessage extends Message {

    public StoCMessage(String nickname) {
        super(nickname);
    }

    /**
     * Write this message to all the given players
     * @param players the players you want to send the message to
     * @return true if the message has been sent to all the players, false if the message hasn't been sent to someone
     */
    public boolean sendBroadcast(List<String> players){
        ControlBase receiver;
        boolean sentToAll = true;
        for(String player : players) {
            receiver = serverCall().findControlBase(player);
            if(receiver == null || !receiver.write(this))
                sentToAll = false;
        }
        return sentToAll;
    }

    /**
     * Write this message to all the players inside the match
     * @param match the match whose players you want to send the message to
     * @return true if the message has been sent to all the players, false if the message hasn't been sent to someone
     */
    public boolean sendBroadcast(Match match){
        ControlBase receiver;
        boolean sentToAll = true;
        for(Player player : match.getPlayers()) {
            receiver = serverCall().findControlBase(player.getNickname());
            if(receiver == null || !receiver.write(this))
                sentToAll = false;
        }
        return sentToAll;
    }

    /**
     * Write this message to the given player
     * @param player the player you want to send the message to
     * @return true if the message has been sent, false if not
     */
    public boolean send(String player){
        ControlBase receiver = serverCall().findControlBase(player);
        if(receiver == null)
            return false;
        return receiver.write(this);
    }

    @Override
    public abstract StoCMessageType getType();

    public abstract boolean compute(Client client);
}