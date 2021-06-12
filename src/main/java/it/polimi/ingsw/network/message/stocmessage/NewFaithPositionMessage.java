package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements a message that notifies every player in the match the new faith
 * position of a certain player.
 * Message structure: { nickname, new position }
 */
public class NewFaithPositionMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEW_FAITH_POSITION;
    private final int newPosition;

    /**
     * Constructor of a new faith position's message.
     * @param nickname the interested of the change
     * @param newPosition its new position
     */
    public NewFaithPositionMessage(String nickname, int newPosition){
        super(nickname);
        this.newPosition = newPosition;
    }

    @Override
    public boolean compute(Client client){
        if(!getNickname().equals("Lorenzo the Magnificent"))
            client.getController().getMatch().setFaithMarker(getNickname(), newPosition);
        else
            client.getController().getMatch().setLorenzoMarker(newPosition);
        return true;
    }

    @Override
    public StoCMessageType getType(){ return type; }

    /**
     * Getter
     * @return the new position
     */
    public int getNewPosition(){ return newPosition; }

}
