package it.polimi.ingsw.network.message.stocmessage;

public class LastRoundMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.LAST_ROUND;
    private final String msg;

    public LastRoundMessage(String nickname, String msg) {
        super(nickname);
        this.msg = msg;
    }

    @Override
    public StoCMessageType getType() { return type; }
    public String getMsg() { return msg; }
}
