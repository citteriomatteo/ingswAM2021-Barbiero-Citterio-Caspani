package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

public class RematchMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.REMATCH;
    private final boolean accepted;


    public RematchMessage(String nickname, boolean accepted){
        super(nickname);
        this.accepted = accepted;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        try {
            return controlBase.getMatchController().response(getNickname(), accepted);
        } catch (RetryException e) {
            controlBase.write(new RetryMessage(getNickname(),e.getError()));
            return false;
        }
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }

    public boolean hasAccepted() { return accepted; }
}
