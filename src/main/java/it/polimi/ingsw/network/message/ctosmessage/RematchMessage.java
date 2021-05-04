package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.server.ControlBase;

public class RematchMessage extends CtoSMessage {
    private final CtoSMessageType type = CtoSMessageType.REMATCH;
    private final boolean accepted;


    public RematchMessage(String nickname, boolean accepted){
        super(nickname);
        this.accepted = accepted;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        return false;
        //TODO
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }

    public boolean hasAccepted() { return accepted; }
}
