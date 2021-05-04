package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.MessageType;

public enum CtoSMessageType implements MessageType
{
    PING,
    BINARY_SELECTION,
    CONFIGURATION,
    LEADERS_CHOICE,
    STARTING_RESOURCES,
    SWITCH_SHELF,
    LEADER_ACTIVATION,
    LEADER_DISCARDING,
    MARKET_DRAW,
    WHITE_MARBLE_CONVERSIONS,
    WAREHOUSE_INSERTION,
    DISCARD_REMAINS,
    DEV_CARD_DRAW,
    PAYMENTS,
    DEV_CARD_PLACEMENT,
    PRODUCTION,
    END_MATCH,
    REMATCH,
    DISCONNECTION,
    END_TURN

//    private final int val;
//    CtoSMessageType(int val){
//        this.val = val;
//    }
//
//    public int getVal(){ return val; }
}
