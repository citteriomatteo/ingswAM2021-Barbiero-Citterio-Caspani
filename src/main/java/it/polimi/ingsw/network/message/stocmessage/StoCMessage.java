package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;

import static it.polimi.ingsw.network.server.ServerUtilities.findControlBase;

public abstract class StoCMessage extends Message {

    public StoCMessage(String nickname) {
        super(nickname);
    }

    /**
     * Write this message to all the given players
     * @param players the players you want to send the message to
     * @return true if the message has been sent, false if something goes wrong
     */
    public boolean sendBroadcast(List<String> players){
        ControlBase receiver;
        for(String player : players) {
            receiver = findControlBase(player);
            if(!receiver.write(this))
                return false;
        }
        return true;
    }

    /**
     * Write this message to all the players inside the match
     * @param match the match whose players you want to send the message to
     * @return true if the message has been sent, false if something goes wrong
     */
    public boolean sendBroadcast(Match match){
        ControlBase receiver;
        for(Player player : match.getPlayers()) {
            receiver = findControlBase(player.getNickname());
            if(!receiver.write(this))
                return false;
        }
        return true;
    }

    /**
     * Write this message to the given player
     * @param player the player you want to send the message to
     * @return true if the message has been sent, false if something goes wrong
     */
    public boolean send(String player){
        ControlBase receiver = findControlBase(player);
        return !receiver.write(this);
    }

    @Override
    public abstract StoCMessageType getType();
}