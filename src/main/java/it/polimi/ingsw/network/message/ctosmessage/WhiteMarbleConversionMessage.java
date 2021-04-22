package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

/**
 * This class implements a message from the client to choose the conversion of the drawn white marbles
 * Message structure: { nickname, the list of Physical resource to be obtained from the conversion }
 */
public class WhiteMarbleConversionMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.WHITE_MARBLE_CONVERSIONS;
    private final List<PhysicalResource> resources;

    public WhiteMarbleConversionMessage(String nickname, List<PhysicalResource> resources) {
        super(nickname);
        this.resources = resources;
    }

    /**
     * Getter
     * @return the list of Physical resource to be obtained from the conversion
     */
    public List<PhysicalResource> getResources() {
        return resources;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }
}
