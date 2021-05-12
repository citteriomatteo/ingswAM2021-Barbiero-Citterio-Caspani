package it.polimi.ingsw.network.message.stocmessage;

public class DevCardDrawnMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DEV_CARD_DRAWN;
    private final String cardID;

    public DevCardDrawnMessage(String nickname, String cardID) {
        super(nickname);
        this.cardID = cardID;
    }

    public String getCardID() { return cardID; }
    @Override
    public  StoCMessageType getType() { return type; }
}
