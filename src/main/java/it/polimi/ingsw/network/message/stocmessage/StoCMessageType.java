package it.polimi.ingsw.network.message.stocmessage;

public enum StoCMessageType {

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
    RETRY (12);

    private int val;
    private StoCMessageType(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
