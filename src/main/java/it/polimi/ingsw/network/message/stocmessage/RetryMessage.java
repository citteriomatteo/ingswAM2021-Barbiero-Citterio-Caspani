package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies the player about a Retry request. Something went wrong.
 * Message structure: { error string }
 */

public class RetryMessage extends StoCMessage {

    private final StoCMessageType type = StoCMessageType.RETRY;
    private final String errMessage;

    public RetryMessage(String nickname, String errMessage){
        super(nickname);
        this.errMessage = errMessage;
    }

    public StoCMessageType getType(){ return type; }
    public String getErrorMessage(){ return errMessage; }

}
