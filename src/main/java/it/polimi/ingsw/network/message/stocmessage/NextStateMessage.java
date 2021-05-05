package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.network.message.Message;

public class NextStateMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEXT_STATE;
    private final StateName newState;

    public NextStateMessage(String nickname, StateName newState){
        super(nickname);
        this.newState = newState;
    }

    public StoCMessageType getType() { return type; }
    public StateName getNewState() { return newState; }

}
