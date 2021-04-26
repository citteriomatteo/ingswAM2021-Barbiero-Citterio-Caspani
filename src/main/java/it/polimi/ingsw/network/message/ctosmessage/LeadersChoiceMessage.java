package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;
import java.util.List;

/**
 * This class implements a message from the client to chose the starting leaders
 * Message structure: { nickname, leaders' Id }
 */
public class LeadersChoiceMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.LEADERS_CHOICE;
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

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
