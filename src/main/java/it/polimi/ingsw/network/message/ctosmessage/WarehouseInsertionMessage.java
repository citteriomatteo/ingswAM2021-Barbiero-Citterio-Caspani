package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the client to insert a PhysicalResource in the selected shelf
 * Message structure: { nickname, resource, the number of the shelf }
 */
public class WarehouseInsertionMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.WAREHOUSE_INSERTION;
    private final PhysicalResource resource;
    private final int shelf;

    public WarehouseInsertionMessage(String nickname, PhysicalResource resource, int shelf) {
        super(nickname);
        this.resource = resource;
        this.shelf = shelf;
    }

    /**
     * Getter
     * @return the PhysicalResource to insert
     */
    public PhysicalResource getResource() {
        return resource;
    }

    /**
     * Getter
     * @return the number of the shelf
     */
    public int getShelf() {
        return shelf;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }
}
