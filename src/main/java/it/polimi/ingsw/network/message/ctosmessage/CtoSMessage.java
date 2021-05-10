package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ControlBase;


public abstract class CtoSMessage extends Message {

    public CtoSMessage(String nickname) {
        super(nickname);
    }

    /**
     * Verifies if is the turn of the player who has sent the message and then executes
     * the command contained in the message on the controller
     * @param controlBase the PlayerHandler which has to execute the message
     * @return true if the execution succeeded
     */
    public abstract boolean computeMessage(ControlBase controlBase);

    @Override
    public abstract CtoSMessageType getType();
}
