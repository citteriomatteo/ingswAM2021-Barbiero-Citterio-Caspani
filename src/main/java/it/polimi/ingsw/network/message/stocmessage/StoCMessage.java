package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;

import static it.polimi.ingsw.network.client.LocalClient.getLocalClient;
import static it.polimi.ingsw.network.server.ServerUtilities.serverCall;

/**
 * This abstract class implements the common structure of every server-to-client message.
 */
public abstract class StoCMessage extends Message {

    public StoCMessage(String nickname) {
        super(nickname);
    }

    /**
     * Writes this message to all the given players or computes it locally if the message is sent during a local play
     * @param players the players you want to send the message to
     */
    public void sendBroadcast(List<String> players){
        ControlBase receiver;

        if(getIsLocal()) {
            receiver = getLocalClient();
            receiver.write(this);
        }
        else {
            for (String player : players) {
                receiver = serverCall().findControlBase(player);
                if (receiver != null)
                    receiver.write(this);
            }
        }
    }

    /**
     * Writes this message to all the players inside the match or computes it locally if the message is sent during a local play
     * @param match the match whose players you want to send the message to
     */
    public void sendBroadcast(Match match){
        ControlBase receiver;

        if(getIsLocal()) {
            receiver = getLocalClient();
            receiver.write(this);
        }
        else {
            for (Player player : match.getPlayers()) {
                receiver = serverCall().findControlBase(player.getNickname());
                if (receiver != null)
                    receiver.write(this);
            }
        }
    }

    /**
     * Write this message to the given player or computes it locally if the message is sent during a local play
     * @param player the player you want to send the message to
     * @return true if the message has been sent, false if not
     */
    public boolean send(String player){
        ControlBase receiver;

        if(getIsLocal())
            receiver = getLocalClient();
        else
            receiver = serverCall().findControlBase(player);

        if(receiver == null)
            return false;

        return receiver.write(this);
    }

    /**
     * Getter
     * @return the StoCMessage's type
     */
    @Override
    public abstract StoCMessageType getType();

    /**
     * Computes the message, causing different behaviours on the Client system.
     * @param client the client
     * @return the result
     */
    public abstract boolean compute(Client client);
}