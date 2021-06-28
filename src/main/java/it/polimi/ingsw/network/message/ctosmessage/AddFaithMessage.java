package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.gameLogic.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This cheat message is used by any admin player to receive a faith point.
 * Message structure: { nickname }
 */
public class AddFaithMessage extends CtoSMessage{
    private static final CtoSMessageType type = CtoSMessageType.ADD_FAITH;

    /**
     * Constructor with comment
     * @param nickname the sender
     */
    public AddFaithMessage(String nickname){
        super(nickname);
    }

    /**
     * Verifies if is the turn of the player who has sent the message and then executes
     * the command contained in the message on the controller
     *
     * @param controlBase the PlayerHandler which has to execute the message
     * @return true if the execution succeeded
     */
    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if(!getNickname().toLowerCase().startsWith("admin")) {
            sendRetryMessage(getNickname(), controlBase, "You can't use this functionality");
            return false;
        }
        else {
            boolean isOk = false;
            try {
                isOk = controlBase.getMatchController().addFaith(getNickname());
            } catch (RetryException e) {
                sendRetryMessage(getNickname(), controlBase, e.getMessage());
            }

            return isOk;
        }


    }

    /**
     * Checks if a variable of the message is null.
     *
     * @return true if it finds a null value
     */
    @Override
    public boolean isSomethingNull() {
        return getNickname() == null;
    }

    /**
     * Returns the message's type.
     *
     * @return the type of the specific message
     */
    @Override
    public CtoSMessageType getType() {
        return CtoSMessageType.ADD_FAITH;
    }
}
