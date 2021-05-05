package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements the message of a leader discard.
 * Message structure: { nickname }
 */
public class DiscardedLeaderMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DISCARDED_LEADER;

    public DiscardedLeaderMessage(String nickname){
        super(nickname);
    }

    public StoCMessageType getType(){
        return type;
    }

}
