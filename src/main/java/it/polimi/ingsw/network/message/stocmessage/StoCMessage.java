package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

public abstract class StoCMessage extends Message {

    public StoCMessage(String nickname) {
        super(nickname);
    }

    /**
     * Write the message on all the players socket
     * @param players the players you want to send the message to
     * @return true if the message has been sent, false if something goes wrong
     */
    public boolean sendBroadcast(List<Player> players){
        return false;
        //Todo
    }

    public boolean send(Player player){
        return false;
        //todo
    }

    @Override
    public abstract StoCMessageType getType();
}