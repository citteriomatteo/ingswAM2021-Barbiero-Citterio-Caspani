package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;


import static it.polimi.ingsw.network.server.ServerUtilities.pushStoCMessage;

public abstract class StoCMessage extends Message {

    public StoCMessage(String nickname) {
        super(nickname);
    }

    /**
     * Tries to add this in the topic linked to the match,
     * if the queue of messages is full, wait since someone else pulls out another CtoS message.
     * After the insertion, Observers (Clients) will be notified.
     * @param match the match to notify
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean send(Match match){
        return pushStoCMessage(match, this);
    }

    @Override
    public abstract StoCMessageType getType();
}