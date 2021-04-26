package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

public class TurnStartedMessage extends Message {

    private final StoCMessageType type = StoCMessageType.YOUR_TURN;
    private final String msg = "It's your turn!";

    public TurnStartedMessage(String nickname){
        super(nickname);
    }

    @Override
    public MessageType getType() {
        return type;
    }
}
