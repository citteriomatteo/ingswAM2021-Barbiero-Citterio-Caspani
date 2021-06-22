package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.gameLogic.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the client to activate the selected leader
 * Message structure: { nickname, leader's Id }
 */
public class LeaderActivationMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.LEADER_ACTIVATION;
    private final String leaderId;

    /**
     * Constructor of the leader activation's message.
     * @param nickname the sender
     * @param leader the id of the chosen leader
     */
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
        if (isSomethingNull()){
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().leaderActivation(getNickname(),leaderId);
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
