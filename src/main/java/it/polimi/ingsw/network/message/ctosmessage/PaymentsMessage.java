package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

/**
 * This class implements a message from the client to pay the costs of a production
 * Message structure: { nickname, list of costs }
 */
public class PaymentsMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.PAYMENTS;
    private final List<PhysicalResource> costs;

    public PaymentsMessage(String nickname, List<PhysicalResource> costs) {
        super(nickname);
        this.costs = costs;
    }

    /**
     * Getter
     * @return the list of costs
     */
    public List<PhysicalResource> getCosts() {
        return costs;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
