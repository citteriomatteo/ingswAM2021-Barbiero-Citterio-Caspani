package it.polimi.ingsw.controller;

public enum StateName {
    //States used in LoginPhase
    LOGIN(0),
    RECONNECTION(0),
    //States used in StartingPhase
    WAITING_LEADERS (1),
    WAITING_RESOURCES (2),
    STARTING_PHASE_DONE (3),
    //States used in TurnController
    STARTING_TURN (4),
    MARKET_ACTION (5),
    INTERMEDIATE (6),
    RESOURCES_PLACEMENT (7),
    BUY_DEV_ACTION (8),
    PLACE_DEV_CARD (9),
    PRODUCTION_ACTION (10),
    END_TURN (11),
    END_MATCH (12),
    REMATCH_OFFER (13),
    NEW_MATCH (14),
    END_GAME (15);


    private int val;
    private StateName(int val){
        this.val = val;
    }

    public int getVal(){ return val; }
}
