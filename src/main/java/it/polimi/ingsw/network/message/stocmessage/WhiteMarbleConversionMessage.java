package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;

public class WhiteMarbleConversionMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEW_WHITE_MARBLE_CONVERSION;
    private final PhysicalResource newConversion;

    public WhiteMarbleConversionMessage(String nickname, PhysicalResource newConversion) {
        super(nickname);
        this.newConversion = newConversion;
    }

    public PhysicalResource getNewConversion() { return newConversion; }
    @Override
    public StoCMessageType getType() { return type; }

}
