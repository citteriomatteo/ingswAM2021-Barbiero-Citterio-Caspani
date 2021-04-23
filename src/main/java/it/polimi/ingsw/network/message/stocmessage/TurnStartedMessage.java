package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

public class TurnStartedMessage extends Message {

    private final StoCMessageType type = StoCMessageType.YOUR_TURN;
    private final String msg = "It's your turn!";

    public TurnStartedMessage(String nickname){
        super(nickname);
    }
}
