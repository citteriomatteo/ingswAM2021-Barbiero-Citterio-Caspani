package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the DevCardDrawnMessage from server to client.
 * Message structure: { nickname, id of the drawn card }
 */
public class DevCardDrawnMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DEV_CARD_DRAWN;
    private final String cardID;

    /**
     * Constructor of a dev card drawn message.
     * @param nickname the receiver
     * @param cardID the leader's ID
     */
    public DevCardDrawnMessage(String nickname, String cardID) {
        super(nickname);
        this.cardID = cardID;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setTempDevCard(getNickname(),cardID);
        return true;
    }

    /**
     * Getter
     * @return the card's ID
     */
    public String getCardID() { return cardID; }

    @Override
    public  StoCMessageType getType() { return type; }
}
