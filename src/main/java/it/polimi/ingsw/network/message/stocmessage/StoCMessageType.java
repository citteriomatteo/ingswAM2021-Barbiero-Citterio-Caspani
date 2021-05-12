package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.MessageType;

public enum StoCMessageType implements MessageType {

    BINARY_SELECTION,
    MARKET_CHANGE,
    CARD_GRID_CHANGE,
    DEV_CARD_SLOT_CHANGE,
    NEW_FAITH_POSITION,
    VATICAN_REPORT,
    WAREHOUSE_CHANGE,
    BUFFER_CHANGE,
    STRONGBOX_CHANGE,
    DISCARDED_LEADER,
    ACTIVATED_LEADER,
    NEW_WHITE_MARBLE_CONVERSION,
    UPDATED_DISCOUNT,
    DEV_CARD_DRAWN,
    TOKEN_DRAW,
    END_GAME_RESULTS,
    RETRY,
    YOUR_TURN,
    REMATCH_OFFERED,
    NEXT_STATE

//    private final int val;
//
//    StoCMessageType(int val){
//        this.val = val;
//    }
//
//    public int getVal(){ return val; }
}
