package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.List;
import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the UpdatedDiscountMapMessage from server to client.
 * Message structure: { nickname, new list of discounts }
 */
public class UpdatedDiscountMapMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEW_WHITE_MARBLE_CONVERSION;
    private final List<PhysicalResource> newDiscountMap;

    /**
     * Constructor of a discount map update's message.
     * @param nickname the nickname
     * @param newDiscountMap the latest discount map version
     */
    public UpdatedDiscountMapMessage(String nickname, List<PhysicalResource> newDiscountMap) {
        super(nickname);
        this.newDiscountMap = newDiscountMap;
    }

    @Override
    public StoCMessageType getType() { return type; }
    /**
     * Getter
     * @return the discount map
     */
    public List<PhysicalResource> getNewDiscountMap() { return newDiscountMap; }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setDiscountMap(getNickname(), newDiscountMap);
        return true;
    }
}
