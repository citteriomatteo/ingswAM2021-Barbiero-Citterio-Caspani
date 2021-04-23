package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies everyone about a leader's play of someone.
 * Message structure: { nickname, ID of the card }
 */

public class ActivatedLeaderMessage extends Message {

    private final StoCMessageType type = StoCMessageType.ACTIVATED_LEADER;
    private final String cardID;

    public ActivatedLeaderMessage(String nickname, String cardID){
        super(nickname);
        this.cardID = cardID;
    }

    public StoCMessageType getType(){
        return type;
    }
    public String getCardID(){
        return cardID;
    }

}
