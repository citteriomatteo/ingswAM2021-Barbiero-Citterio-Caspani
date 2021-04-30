package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.network.message.Message;

import static it.polimi.ingsw.network.server.ServerUtilities.pushCtoSMessage;

public abstract class CtoSMessage extends Message {

    public CtoSMessage(String nickname) {
        super(nickname);
    }

    /**
     * Tries to add this in the topic linked to the match,
     * if the queue of messages is full, wait since someone else pulls out another CtoS message
     * @param match the match to notify
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean send(Match match){
        return pushCtoSMessage(match, this);
    }

    @Override
    public abstract CtoSMessageType getType();
}
