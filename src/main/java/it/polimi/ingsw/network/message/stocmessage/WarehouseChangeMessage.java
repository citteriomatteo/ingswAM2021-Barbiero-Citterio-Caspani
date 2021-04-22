package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that updates the warehouse of a certain player on every client's VIEW.
 * Message structure: {nickname, new warehouse}
 */

public class WarehouseChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.WAREHOUSE_CHANGE;
    private String nickname;
    private Warehouse newWarehouse;

    public WarehouseChangeMessage(String receiver, String nickname, Warehouse newWarehouse){
        super(receiver);
        this.nickname = nickname;
        this.newWarehouse = newWarehouse;
    }

    private StoCMessageType getType(){ return type; }
    private String getNickname(){ return nickname; }
    private Warehouse getNewWarehouse(){ return newWarehouse; }

}
