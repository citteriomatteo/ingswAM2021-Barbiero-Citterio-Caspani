package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.List;

public class UpdatedDiscountMapMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.NEW_WHITE_MARBLE_CONVERSION;
    private final List<PhysicalResource> newDiscountMap;

    public UpdatedDiscountMapMessage(String nickname, List<PhysicalResource> newDiscountMap) {
        super(nickname);
        this.newDiscountMap = newDiscountMap;
    }

    @Override
    public StoCMessageType getType() { return type; }
    public List<PhysicalResource> getNewDiscountMap() { return newDiscountMap; }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setDiscountMap(getNickname(), newDiscountMap);
        return true;
    }
}
