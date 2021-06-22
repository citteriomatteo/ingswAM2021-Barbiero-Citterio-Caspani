package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the WhiteMarbleConversionMessage from server to client.
 *
 * Message structure: { nickname, new conversion }
 */
public class WhiteMarbleConversionMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEW_WHITE_MARBLE_CONVERSION;
    private final PhysicalResource newConversion;

    /**
     * Constructor of a white marble conversions' update message.
     * @param nickname the receiver
     * @param newConversion the new conversion to add into the map
     */
    public WhiteMarbleConversionMessage(String nickname, PhysicalResource newConversion) {
        super(nickname);
        this.newConversion = newConversion;
    }

    @Override
    public StoCMessageType getType() { return type; }
    /**
     * Getter
     * @return the new conversion
     */
    public PhysicalResource getNewConversion() { return newConversion; }

    @Override
    public boolean compute(Client client) {
        client.getController().getMatch().setWhiteMarbleConversions(getNickname(), this.newConversion);
        return true;
    }

}
