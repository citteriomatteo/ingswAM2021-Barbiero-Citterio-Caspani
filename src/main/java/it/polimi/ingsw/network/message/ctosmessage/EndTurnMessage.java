package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.gameLogic.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements the EndTurnMessage, to notify the players involved in nextTurn operation about their next states.
 */
public class EndTurnMessage extends CtoSMessage {

    private static final CtoSMessageType type = CtoSMessageType.END_TURN;

    /**
     * Constructor of the end turn message, sent when the current player finishes its turn.
     * @param nickname the sender
     */
    public EndTurnMessage(String nickname){
        super(nickname);
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()) {
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().nextTurn(getNickname());
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null;
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }

}
