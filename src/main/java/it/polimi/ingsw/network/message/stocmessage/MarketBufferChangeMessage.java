package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.client.Client;

import java.util.List;

/**
 * This class implements the MarketBufferChangeMessage from server to client.
 * Message structure: { nickname, selection, list of buffered resources }
 */
public class MarketBufferChangeMessage extends StoCMessage{

    private static final StoCMessageType type = StoCMessageType.BUFFER_CHANGE;
    private final List<PhysicalResource> newBuffer;

    /**
     * Constructor of a market buffer change message.
     * @param nickname the receiver
     * @param newBuffer the new buffer state
     */
    public MarketBufferChangeMessage(String nickname, List<PhysicalResource> newBuffer){
        super(nickname);
        this.newBuffer = newBuffer;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setMarketBuffer(getNickname(), newBuffer);
        return true;
    }

    @Override
    public StoCMessageType getType() {
        return type;
    }

    /**
     * Getter
     * @return the new buffer state
     */
    public List<PhysicalResource> getNewBuffer() {
        return newBuffer;
    }
}
