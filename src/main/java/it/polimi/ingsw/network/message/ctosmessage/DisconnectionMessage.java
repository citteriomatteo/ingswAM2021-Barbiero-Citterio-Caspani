package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.DisconnectionException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements the DisconnectionMessage.
 */
public class DisconnectionMessage extends CtoSMessage{
    private static final CtoSMessageType type = CtoSMessageType.DISCONNECTION;

    public DisconnectionMessage(String nickname) {
        super(nickname);
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        throw new DisconnectionException("Player " + super.getNickname() + " disconnected politely", true);
    }

    @Override
    public boolean isSomethingNull() {
        return false;
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }
}
