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
        try {
            return controlBase.getMatchController().leaderDiscarding(getNickname(),leaderId);
        } catch (RetryException e) {
            new RetryMessage(getNickname(), controlBase.getMatchController().getCurrentState(getNickname()), e.getError()).send(getNickname());
            return false;
        }
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
