package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;

/**
 * This class implements a message from the client to chose the starting leaders
 * Message structure: { nickname, leaders' Id }
 */
public class LeadersChoiceMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.LEADERS_CHOICE;
    private final List<String> leaderIds;

    public LeadersChoiceMessage(String nickname, List<String> leaderIds) {
        super(nickname);
        this.leaderIds = leaderIds;
    }

    /**
     * Getter
     * @return the list of leaders' Id
     */
    public List<String> getLeaders() {
        return leaderIds;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()){
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().startingLeader(getNickname(),leaderIds);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null || leaderIds == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
