package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

public class EndTurnMessage extends Message {

    private final CtoSMessageType type = CtoSMessageType.END_TURN;


    public EndTurnMessage(String nickname){
        super(nickname);
    }

    @Override
    public MessageType getType() {
        return type;
    }

}
