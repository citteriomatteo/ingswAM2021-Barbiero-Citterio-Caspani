package it.polimi.ingsw.network.message.stocmessage;

import java.util.List;

public class HandLeadersStateMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.HAND_LEADERS_STATE;
    private final List<String> handLeaders;

    public HandLeadersStateMessage(String nickname, List<String> handLeaders) {
        super(nickname);
        this.handLeaders = handLeaders;
    }

    public List<String> getHandLeaders() { return handLeaders; }
    @Override
    public StoCMessageType getType() { return type; }
}
