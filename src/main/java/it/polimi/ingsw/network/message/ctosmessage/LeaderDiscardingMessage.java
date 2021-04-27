package it.polimi.ingsw.network.message.ctosmessage;


import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the client to discard the selected leader
 * Message structure: { nickname, leader's Id }
 */
public class LeaderDiscardingMessage extends Message {
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

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
