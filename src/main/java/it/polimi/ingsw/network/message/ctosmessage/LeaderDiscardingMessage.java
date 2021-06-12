package it.polimi.ingsw.network.message.ctosmessage;


import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the client to discard the selected leader
 * Message structure: { nickname, leader's Id }
 */
public class LeaderDiscardingMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.LEADER_DISCARDING;
    private final String leaderId;

    /**
     * Constructor of the leader discarding message.
     * @param nickname the sender
     * @param leaderId the id of the discarded leader
     */
    public LeaderDiscardingMessage(String nickname, String leaderId) {
        super(nickname);
        this.leaderId = leaderId;
    }

    /**
     * Getter
     * @return the leader's Id
     */
    public String getLeader() {
        return leaderId;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()) {
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().leaderDiscarding(getNickname(),leaderId);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null || leaderId == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
