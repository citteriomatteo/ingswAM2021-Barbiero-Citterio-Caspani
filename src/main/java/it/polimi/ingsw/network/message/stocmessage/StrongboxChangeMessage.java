package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

/**
 * This class implements a message that updates the strongbox of a certain player on every client's VIEW.
 * Message structure: {nickname, new strongbox}
 */
public class StrongboxChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.STRONGBOX_CHANGE;
    private final List<PhysicalResource> newStrongbox;

    public StrongboxChangeMessage(String nickname, List<PhysicalResource> newStrongbox){
        super(nickname);
        this.newStrongbox = newStrongbox;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setStrongbox(getNickname(), newStrongbox);
        return true;
    }
    @Override
    public StoCMessageType getType(){ return type; }
    public List<PhysicalResource> getNewStrongbox(){ return newStrongbox; }

}
