package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;

public class AcknowledgementMessage extends Message {

    private final CtoSMessageType type = CtoSMessageType.ACKNOWLEDGEMENT;

    public AcknowledgementMessage(String nickname){
        super(nickname);
    }

    @Override
    public MessageType getType() {
        return type;
    }

}
