package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that updates the strongbox of a certain player on every client's VIEW.
 * Message structure: {nickname, new strongbox}
 */


public class StrongboxChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.STRONGBOX_CHANGE;
    private String nickname;
    private StrongBox newStrongbox;

    public StrongboxChangeMessage(String receiver, String nickname, StrongBox newStrongbox){
        super(receiver);
        this.nickname = nickname;
        this.newStrongbox = newStrongbox;
    }

    private StoCMessageType getType(){ return type; }
    private String getNickname(){ return nickname; }
    private StrongBox getNewStrongbox(){ return newStrongbox; }

}
