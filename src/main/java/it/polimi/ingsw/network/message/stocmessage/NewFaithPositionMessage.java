package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies every player in the match the new faith
 * position of a certain player.
 * Message structure: { nickname, new position }
 */

public class NewFaithPositionMessage extends Message {

    private final StoCMessageType type = StoCMessageType.NEW_FAITH_POSITION;
    private String nickname;
    private int newPosition;

    public NewFaithPositionMessage(String receiver, String nickname, int newPosition){
        super(receiver);
        this.nickname = nickname;
        this.newPosition = newPosition;
    }

    private StoCMessageType getType(){ return type; }
    private String getNickname(){ return nickname; }
    private int getNewPosition(){ return newPosition; }

}
