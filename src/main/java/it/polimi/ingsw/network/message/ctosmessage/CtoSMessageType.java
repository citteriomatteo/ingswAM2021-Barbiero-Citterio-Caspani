package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.MessageType;

public enum CtoSMessageType implements MessageType
{
    PING(0),
    LOGIN(0),
    NUM_PLAYERS(0),
    BINARY_SELECTION(0),
    CONFIGURE(0),
    LEADERS_CHOICE(2),
    STARTING_RESOURCES(2),
    SWITCH_SHELF(2),
    LEADER_ACTIVATION(2),
    LEADER_DISCARDING(2),
    MARKET_DRAW(2),
    WHITE_MARBLE_CONVERSIONS(2),
    WAREHOUSE_INSERTION(2),
    DISCARD_REMAINS(2),
    DEV_CARD_DRAW(2),
    PAYMENTS(2),
    DEV_CARD_PLACEMENT(2),
    PRODUCTION(2),
    REMATCH(2),
    DISCONNECTION(0),
    END_TURN(2);

    private final int code;

    CtoSMessageType(int code){
        this.code = code;
    }

    /**
     * Returns a code that indicate in witch phase the message can be handled:
     * 0: every phase
     * 1: initialization phase (before the game start)
     * 2: game phase (the messages require a matchController to work)
     * @return the code
     */
    public int getCode(){ return code; }
}
