package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that updates the strongbox of a certain player on every client's VIEW.
 * Message structure: {nickname, new strongbox}
 */


public class StrongboxChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.STRONGBOX_CHANGE;
    private final StrongBox newStrongbox;

    public StrongboxChangeMessage(String nickname, StrongBox newStrongbox){
        super(nickname);
        this.newStrongbox = newStrongbox;
    }

    public StoCMessageType getType(){ return type; }
    public StrongBox getNewStrongbox(){ return newStrongbox; }

}
