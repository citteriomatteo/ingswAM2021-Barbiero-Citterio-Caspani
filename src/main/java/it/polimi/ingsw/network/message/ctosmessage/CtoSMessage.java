package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import static it.polimi.ingsw.view.ClientController.getClientController;

/**
 * This class implements the the common client-to-server message.
 */
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

    /**
     * Controls if this message can be sent to the server and then sends it
     * @return true if the message has been sent
     */
    public boolean send(){
        return getClientController().send(this);
    }

    public abstract boolean isSomethingNull();

    @Override
    public abstract CtoSMessageType getType();

    public void sendRetryMessage(String nickname,ControlBase controlBase,  String error){
        new RetryMessage(nickname, controlBase.getCurrentState(), error).send(nickname);
    }
}
