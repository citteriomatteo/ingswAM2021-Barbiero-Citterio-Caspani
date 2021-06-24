package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.gameLogic.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the player to order to place the development card(identified by its Id)
 * in the selected column of the devCardSlots
 * Message structure: { nickname, row's number, column's number }
 */
public class DevCardPlacementMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.DEV_CARD_PLACEMENT;
    private final int column;

    /**
     * Constructor of the dev card placement message.
     * @param nickname the sender
     * @param column the chosen column number
     */
    public DevCardPlacementMessage(String nickname, int column) {
        super(nickname);
        this.column = column;
    }

    /**
     * Getter
     * @return the number of the devCardSlots' column in which insert the card
     */
    public int getColumn() {
        return column;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()) {
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().devCardPlacement(getNickname(),column);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getMessage());
            return false;
        }

    }

    @Override
    public boolean isSomethingNull() {
        return getNickname() == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
