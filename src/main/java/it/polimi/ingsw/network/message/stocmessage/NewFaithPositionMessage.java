package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies every player in the match the new faith
 * position of a certain player.
 * Message structure: { nickname, new position }
 */

public class NewFaithPositionMessage extends Message {

    private final StoCMessageType type = StoCMessageType.NEW_FAITH_POSITION;
    private final int newPosition;

    public NewFaithPositionMessage(String nickname, int newPosition){
        super(nickname);
        this.newPosition = newPosition;
    }

    private StoCMessageType getType(){ return type; }
    private int getNewPosition(){ return newPosition; }

}
