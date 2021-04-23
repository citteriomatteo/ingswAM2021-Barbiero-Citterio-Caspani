package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that updates the warehouse of a certain player on every client's VIEW.
 * Message structure: {nickname, new warehouse}
 */

public class WarehouseChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.WAREHOUSE_CHANGE;
    private final Warehouse newWarehouse;

    public WarehouseChangeMessage(String nickname, Warehouse newWarehouse){
        super(nickname);
        this.newWarehouse = newWarehouse;
    }

    public StoCMessageType getType(){ return type; }
    public Warehouse getNewWarehouse(){ return newWarehouse; }

}
