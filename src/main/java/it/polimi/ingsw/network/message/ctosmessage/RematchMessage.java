package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

public class RematchMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.REMATCH;
    private final boolean accepted;


    public RematchMessage(String nickname, boolean accepted){
        super(nickname);
        this.accepted = accepted;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public boolean hasAccepted() { return accepted; }
}
