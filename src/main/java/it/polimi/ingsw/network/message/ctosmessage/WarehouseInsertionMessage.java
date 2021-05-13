package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

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

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull())
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
        try {
            return controlBase.getMatchController().warehouseInsertion(getNickname(), resources);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getError());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null || resources == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }
}
