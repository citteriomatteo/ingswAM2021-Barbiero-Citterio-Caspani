package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a message from the client to choose the initial bonus PhysicalResource
 * Message structure: { nickname, the chosen resource }
 */
public class StartingResourcesMessage extends Message {
    private static final CtoSMessageType type = CtoSMessageType.STARTING_RESOURCES;
    private final List<PhysicalResource> resources;

    public StartingResourcesMessage(String nickname, List<PhysicalResource> resources) {
        super(nickname);
        this.resources = resources;
    }

    /**
     * Getter
     * @return the chosen bonus resource
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
