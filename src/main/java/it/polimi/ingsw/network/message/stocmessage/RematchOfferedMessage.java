package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

public class RematchOfferedMessage extends StoCMessage {

    private final StoCMessageType type = StoCMessageType.REMATCH_OFFERED;
    private final String message;

    public RematchOfferedMessage(String nickname, String proposer) {
        super(nickname);
        this.message = proposer+" offered a rematch.\nAccept? [y/n]";
    }

    public StoCMessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
