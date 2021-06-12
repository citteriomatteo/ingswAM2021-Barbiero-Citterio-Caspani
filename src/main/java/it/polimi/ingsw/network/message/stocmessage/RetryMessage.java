package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.network.client.Client;

/**
 * This class implements a message that notifies the player about a Retry request. Something went wrong.
 * Message structure: { error string }
 */
public class RetryMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.RETRY;
    private StateName currentState;
    private final String errMessage;

    /**
     * Constructor of a retry message.
     * @param nickname the receiver
     * @param currentState the current state after the retry (the same as before, for a force reset)
     * @param errMessage an error message
     */
    public RetryMessage(String nickname, StateName currentState, String errMessage){
        super(nickname);
        this.currentState = currentState;
        this.errMessage = errMessage;
    }

    @Override
    public boolean compute(Client client){
        //todo: fix this, watch printRetry
      //  client.getController().printRetry(errMessage, currentState);
        client.getController().updateCurrentState(this);
        return true;
    }
    @Override
    public StoCMessageType getType(){ return type; }
    /**
     * Getter
     * @return the error message
     */
    public String getErrorMessage(){ return errMessage; }
    /**
     * Getter
     * @return the current state
     */
    public StateName getCurrentState(){ return currentState; }

}
