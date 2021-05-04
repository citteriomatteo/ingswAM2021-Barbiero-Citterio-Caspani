package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

/**
 * This class implements a message from the client to insert a list of ResTypes in the selected shelf
 * Message structure: { nickname, list of PhysicalResource where the ResType is the actual resource,
 * and the int value is the number of the shelf }
 */
public class WarehouseInsertionMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.WAREHOUSE_INSERTION;
    private final List<PhysicalResource> resources;



    public WarehouseInsertionMessage(String nickname, List<PhysicalResource> resources) {
        super(nickname);
        this.resources = resources;
    }

    /**
     * Getter
     * @return the list of PhysicalResource where the ResType is the actual resource,
     *  and the int value is the number of the shelf
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
