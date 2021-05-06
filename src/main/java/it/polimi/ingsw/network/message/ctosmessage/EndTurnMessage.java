package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

public class EndTurnMessage extends CtoSMessage {

    private static final CtoSMessageType type = CtoSMessageType.END_TURN;


    public EndTurnMessage(String nickname){
        super(nickname);
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        try {
            return controlBase.getMatchController().nextTurn(getNickname());
        } catch (RetryException e) {
            controlBase.write(new RetryMessage(getNickname(),e.getError()));
            return false;
        }
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }

}
