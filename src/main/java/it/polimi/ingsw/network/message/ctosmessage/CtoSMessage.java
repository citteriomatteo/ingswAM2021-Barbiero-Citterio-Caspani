package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import static it.polimi.ingsw.view.ClientController.getClientController;

/**
 * This class implements the the common client-to-server message.
 */
public abstract class CtoSMessage extends Message {

    /**
     * Constructor for the CtoSMessage, which only takes the nickname of the player that is writing to the server.
     * @param nickname the sender's nickname
     */
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

    /**
     * Checks if a variable of the message is null.
     * @return true if it finds a null value
     */
    public abstract boolean isSomethingNull();

    /**
     * Returns the message's type.
     * @return the type of the specific message
     */
    @Override
    public abstract CtoSMessageType getType();

    /**
     * Creates a retry message and sends it back immediately to the client.
     * @param nickname the receiver
     * @param controlBase interface used to get the current player
     * @param error the error message
     */
    public void sendRetryMessage(String nickname, ControlBase controlBase,  String error){
        new RetryMessage(nickname, controlBase.getCurrentState(), error).send(nickname);
    }
}
