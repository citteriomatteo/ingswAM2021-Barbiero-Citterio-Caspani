package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the message of a leader discard.
 * Message structure: { nickname }
 */
public class DiscardedLeaderMessage extends StoCMessage {
    private static final StoCMessageType type = StoCMessageType.DISCARDED_LEADER;

    /**
     * Constructor of a discarded leader notify message.
     * @param nickname the receiver
     */
    public DiscardedLeaderMessage(String nickname){
        super(nickname);
    }

    @Override
    public boolean compute(Client client){
        if(! client.getNickname().equals(getNickname()))
            client.getController().getMatch().leaderDiscard(getNickname());
        return true;
    }

    @Override
    public StoCMessageType getType(){
        return type;
    }

}
