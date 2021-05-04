package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.server.ControlBase;

public class EndTurnMessage extends CtoSMessage {

    private final CtoSMessageType type = CtoSMessageType.END_TURN;


    public EndTurnMessage(String nickname){
        super(nickname);
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

}
