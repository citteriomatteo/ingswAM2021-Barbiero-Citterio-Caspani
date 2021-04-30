package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;

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

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
