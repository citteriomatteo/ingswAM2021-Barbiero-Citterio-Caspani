package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.MessageType;

public enum CtoSMessageType implements MessageType
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
    DISCARD_REMAINS (10),
    DEV_CARD_DRAW (11),
    PAYMENTS (12),
    DEV_CARD_PLACEMENT (13),
    PRODUCTION (14),
    END_MATCH (15),
    REMATCH_OFFER (16),
    REMATCH_RESPONSE (17),
    DISCONNECTION (18);

    private final int val;
    CtoSMessageType(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
