package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements a message that notifies everyone about a leader's play of someone.
 * Message structure: { nickname, ID of the card }
 */
public class ActivatedLeaderMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.ACTIVATED_LEADER;
    private final String cardID;

    public ActivatedLeaderMessage(String nickname, String cardID){
        super(nickname);
        this.cardID = cardID;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().activateLeader(getNickname(), cardID);
        return true;
    }

    public StoCMessageType getType(){
        return type;
    }
    public String getCardID(){
        return cardID;
    }

}
