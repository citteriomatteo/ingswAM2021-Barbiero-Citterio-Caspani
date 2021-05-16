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

    public NewFaithPositionMessage(String nickname, int newPosition){
        super(nickname);
        this.newPosition = newPosition;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setFaithMarker(getNickname(), newPosition);
        return true;
    }

    @Override
    public StoCMessageType getType(){ return type; }
    public int getNewPosition(){ return newPosition; }

}
