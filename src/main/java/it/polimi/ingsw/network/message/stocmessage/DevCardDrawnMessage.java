package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

public class DevCardDrawnMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DEV_CARD_DRAWN;
    private final String cardID;

    public DevCardDrawnMessage(String nickname, String cardID) {
        super(nickname);
        this.cardID = cardID;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setTempDevCard(getNickname(),cardID);
        return true;
    }

    public String getCardID() { return cardID; }
    @Override
    public  StoCMessageType getType() { return type; }
}
