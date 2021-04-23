package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.MessageType;

public enum StoCMessageType implements MessageType {

    MARKET_CHANGE (1),
    CARD_GRID_CHANGE (2),
    DEV_CARD_SLOT_CHANGE (3),
    NEW_FAITH_POSITION (4),
    VATICAN_REPORT (5),
    WAREHOUSE_CHANGE (6),
    STRONGBOX_CHANGE (7),
    DISCARDED_LEADER (8),
    ACTIVATED_LEADER (9),
    TOKEN_DRAW (10),
    END_GAME_RESULTS (11),
    RETRY (12),
    YOUR_TURN (13),
    REMATCH_OFFERED (14);

    private final int val;

    StoCMessageType(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
