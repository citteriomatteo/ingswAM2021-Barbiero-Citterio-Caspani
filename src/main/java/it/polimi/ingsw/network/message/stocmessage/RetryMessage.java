package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies the player about a Retry request. Something went wrong.
 * Message structure: { error string }
 */

public class RetryMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.RETRY;
    private StateName currentState;
    private final String errMessage;

    public RetryMessage(String nickname, StateName currentState, String errMessage){
        super(nickname);
        this.currentState = currentState;
        this.errMessage = errMessage;
    }

    public RetryMessage(String nickname, String errMessage){
        super(nickname);
        this.currentState = null;
        this.errMessage = errMessage;
    }

    public StoCMessageType getType(){ return type; }
    public String getErrorMessage(){ return errMessage; }
    public StateName getCurrentState(){ return currentState; }

}
