package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the NextStateMessage from server to client.
 * Notifies the player about his next in-game possibilities.
 * Message structure: { nickname, new state }
 */
public class NextStateMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEXT_STATE;
    private final StateName newState;

    /**
     * Constructor of the next state's message.
     * @param nickname the receiver
     * @param newState the new state
     */
    public NextStateMessage(String nickname, StateName newState){
        super(nickname);
        this.newState = newState;
    }

    @Override
    public boolean compute(Client client){
        client.getController().updateCurrentState(this);
        return true;
    }
    @Override
    public StoCMessageType getType() { return type; }

    /**
     * Getter
     * @return the new state of the player
     */
    public StateName getNewState() { return newState; }

}
