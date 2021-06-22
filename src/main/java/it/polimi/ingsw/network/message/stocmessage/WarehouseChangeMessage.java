package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.client.Client;

import java.util.List;

/**
 * This class implements a message that updates the warehouse of a certain player on every client's VIEW.
 * Message structure: {nickname, new warehouse}
 */
public class WarehouseChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.WAREHOUSE_CHANGE;
    private final List<PhysicalResource> newWarehouse;

    /**
     * Constructor of a warehouse update's message.
     * @param nickname the nickname
     * @param newWarehouse the latest warehouse's version
     */
    public WarehouseChangeMessage(String nickname, List<PhysicalResource> newWarehouse){
        super(nickname);
        this.newWarehouse = newWarehouse;
    }
    @Override
    public StoCMessageType getType(){ return type; }
    /**
     * Getter
     * @return the new warehouse
     */
    public List<PhysicalResource> getNewWarehouse(){ return newWarehouse; }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setWarehouse(getNickname(), newWarehouse);
        return true;
    }

}
