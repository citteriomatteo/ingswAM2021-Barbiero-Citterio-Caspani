package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

public class PlayerConnectionState extends StoCMessage{
    private static final StoCMessageType type = StoCMessageType.PLAYER_DISCONNECTED;
    private boolean connected;

    public PlayerConnectionState(String nickname, boolean connected) {
        super(nickname);
        this.connected = connected;
    }


    @Override
    public StoCMessageType getType() {
        return type;
    }

    @Override
    public boolean compute(Client client) {
        client.getController().getMatch().setConnected(getNickname(), connected);
        return false;
    }

    public boolean getConnected(){
        return connected;
    }
}
