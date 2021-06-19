package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements the RematchMessage.
 * Message structure: { nickname, accepted/declined }
 */
public class RematchMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.REMATCH;
    private final boolean accepted;

    /**
     * Constructor of a rematch message.
     * @param nickname the sender
     * @param accepted boolean containing the choice of the player
     */
    public RematchMessage(String nickname, boolean accepted){
        super(nickname);
        this.accepted = accepted;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()){
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().response(getNickname(), accepted);
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

    /**
     * Getter
     * @return the choice of the player
     */
    public boolean hasAccepted() { return accepted; }
}
