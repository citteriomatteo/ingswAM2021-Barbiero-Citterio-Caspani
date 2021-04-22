package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies everyone about a leader's play of someone.
 * Message structure: { nickname, ID of the card }
 */

public class ActivatedLeaderMessage extends Message {

    private final StoCMessageType type = StoCMessageType.ACTIVATED_LEADER;
    private String nickname;
    private String cardID;

    public ActivatedLeaderMessage(String receiver, String nickname, String cardID){
        super(receiver);
        this.nickname = nickname;
        this.cardID = cardID;
    }

}
