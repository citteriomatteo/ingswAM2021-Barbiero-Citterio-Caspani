package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the client to activate the selected leader
 * Message structure: { nickname, leader's Id }
 */
public class LeaderActivationMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.LEADER_ACTIVATION;
    private final String leaderId;

    public LeaderActivationMessage(String nickname, String leader) {
        super(nickname);
        this.leaderId = leader;
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
            return controlBase.getMatchController().leaderActivation(getNickname(),leaderId);
        } catch (RetryException e) {
            controlBase.write(new RetryMessage(getNickname(),e.getError()));
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
