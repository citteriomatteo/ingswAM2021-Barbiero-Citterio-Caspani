package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;
import java.util.Map;

/**
 * This class implements a message from the client to pay the costs of a production
 * Message structure: { nickname, list of costs }
 */
public class PaymentsMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.PAYMENTS;
    private final List<PhysicalResource> strongboxCosts;
    private final Map<Integer,PhysicalResource> warehouseCosts;

    public PaymentsMessage(String nickname, List<PhysicalResource> strongboxCosts, Map<Integer,PhysicalResource> warehouseCosts) {
        super(nickname);
        this.warehouseCosts = warehouseCosts;
        this.strongboxCosts = strongboxCosts;
    }

    /**
     * Getter
     * @return the map of costs from warehouse
     */
    public Map<Integer, PhysicalResource> getWarehouseCosts() {
        return warehouseCosts;
    }

    /**
     * Getter
     * @return the list of costs
     */
    public List<PhysicalResource> getStrongboxCosts() {
        return strongboxCosts;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull())
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
        try {
            return controlBase.getMatchController().payments(getNickname(), strongboxCosts, warehouseCosts);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getError());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null || warehouseCosts == null || strongboxCosts == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
