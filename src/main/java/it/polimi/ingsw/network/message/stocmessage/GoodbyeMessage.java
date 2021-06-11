package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the GoodbyeMessage from server to client, for disconnection or end of match with no rematch.
 * Message structure: { nickname, string-made message, abnormal condition flag }
 */
public class GoodbyeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.GOODBYE;
    private final String msg;
    private final boolean abnormalCondition;

    public GoodbyeMessage(String nickname, String msg, boolean abnormalCondition) {
        super(nickname);
        this.msg = msg;
        this.abnormalCondition = abnormalCondition;
    }

    @Override
    public boolean compute(Client client){
        client.getController().printGoodbyeMessage(msg);
        return true;
    }
    @Override
    public StoCMessageType getType() { return type; }
    public String getMsg() { return msg; }
    public boolean isAbnormalCondition() {
        return abnormalCondition;
    }
}
