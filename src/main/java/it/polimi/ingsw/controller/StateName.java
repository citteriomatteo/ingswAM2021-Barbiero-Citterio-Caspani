package it.polimi.ingsw.controller;

public enum StateName {
    STARTING_TURN (1),
    MARKET_ACTION (2),
    WHITE_MARBLES_STATE (3),
    NOT_DISCARDABLE_RESOURCES (4),
    BUY_DEV_ACTION (5),
    PLACE_DEV_CARD (6),
    PRODUCTION_ACTION (7),
    END_TURN (8),
    END_MATCH (9),
    REMATCH_OFFER (10),
    NEW_MATCH (11),
    END_GAME (12);


    private int val;
    private StateName(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
