package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.List;

public class MarketBufferChangeMessage extends StoCMessage{

    private static final StoCMessageType type = StoCMessageType.BUFFER_CHANGE;
    private final List<PhysicalResource> newBuffer;

    public MarketBufferChangeMessage(String nickname, List<PhysicalResource> newBuffer){
        super(nickname);
        this.newBuffer = newBuffer;
    }

    public List<PhysicalResource> getNewBuffer() {
        return newBuffer;
    }
    @Override
    public StoCMessageType getType() {
        return type;
    }
}
