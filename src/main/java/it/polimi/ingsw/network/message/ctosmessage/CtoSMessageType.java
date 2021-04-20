package it.polimi.ingsw.network.message.ctosmessage;

public enum CtoSMessageType
{
    CONFIGURATION (1),
    LEADERS_CHOICE (2),
    STARTING_RESOURCES (3),
    SWITCH_SHELF (4),
    LEADER_ACTIVATION (5),
    LEADER_DISCARDING (6),
    MARKET_DRAW (7),
    WHITE_MARBLE_CONVERSIONS (8),
    WAREHOUSE_INSERTION (9),
    DEV_CARD_DRAW (10),
    PAYMENTS (11),
    DEV_CARD_PLACEMENT (12),
    PRODUCTION (13);

    private int val;
    private CtoSMessageType(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
