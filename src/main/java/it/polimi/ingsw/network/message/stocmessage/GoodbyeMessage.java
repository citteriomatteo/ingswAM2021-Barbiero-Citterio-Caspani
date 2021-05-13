package it.polimi.ingsw.network.message.stocmessage;

public class GoodbyeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.GOODBYE;
    private final String msg;

    public GoodbyeMessage(String nickname, String msg) {
        super(nickname);
        this.msg = msg;
    }

    @Override
    public StoCMessageType getType() { return type; }
    public String getMsg() { return msg; }
}
