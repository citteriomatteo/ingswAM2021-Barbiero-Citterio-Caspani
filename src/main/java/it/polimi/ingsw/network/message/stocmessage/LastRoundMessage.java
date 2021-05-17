package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

public class LastRoundMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.LAST_ROUND;

    public LastRoundMessage(String nickname, String msg) {
        super(nickname);
    }

    @Override
    public boolean compute(Client client){
        client.getController().printLastRound();
        return true;
    }

    @Override
    public StoCMessageType getType() { return type; }
}
