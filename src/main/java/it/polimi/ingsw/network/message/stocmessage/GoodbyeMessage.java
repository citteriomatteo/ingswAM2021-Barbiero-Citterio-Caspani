package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

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
        System.out.println(msg);
        return true;
    }
    @Override
    public StoCMessageType getType() { return type; }
    public String getMsg() { return msg; }
    public boolean isAbnormalCondition() {
        return abnormalCondition;
    }
}
