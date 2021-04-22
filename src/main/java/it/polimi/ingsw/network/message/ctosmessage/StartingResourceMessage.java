package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the client to choose the initial bonus PhysicalResource
 * Message structure: { nickname, the chosen resource }
 */
public class StartingResourceMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.STARTING_RESOURCES;
    private final PhysicalResource resource;

    public StartingResourceMessage(String nickname, PhysicalResource resource) {
        super(nickname);
        this.resource = resource;
    }

    /**
     * Getter
     * @return the chosen bonus resource
     */
    public PhysicalResource getResource() {
        return resource;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }
}
