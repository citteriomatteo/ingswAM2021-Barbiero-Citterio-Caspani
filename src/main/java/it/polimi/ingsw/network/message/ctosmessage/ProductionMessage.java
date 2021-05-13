package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import java.util.List;

/**
 * This class implements a message from the client for starting a production
 * Message structure: { nickname, leaders' Id, a production the contains the translation of the unknown resources }
 */
public class ProductionMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.PRODUCTION;
    private final List<String> cardIds;
    private final Production productionOfUnknown;

    public ProductionMessage(String nickname, List<String> cardIds, Production productionOfUnknown) {
        super(nickname);
        this.cardIds = cardIds;
        this.productionOfUnknown = productionOfUnknown;
    }

    /**
     * Getter
     * @return the leaders' Id
     */
    public List<String> getCardIds() {
        return cardIds;
    }

    /**
     * Getter
     * @return the production the contains the translation of the unknown resources
     */
    public Production getProductionOfUnknown() {
        return productionOfUnknown;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()) {
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().production(getNickname(), cardIds, productionOfUnknown);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getError());
            return false;
        }
    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null || cardIds == null || productionOfUnknown == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
