package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the player to order to place the development card(identified by its Id)
 * in the selected column of the devCardSlots
 * Message structure: { nickname, row's number, column's number }
 */

public class DevCardPlacementMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.DEV_CARD_PLACEMENT;
    private final int column;

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
        try {
            return controlBase.getMatchController().devCardPlacement(getNickname(),column);
        } catch (RetryException e) {
            new RetryMessage(getNickname(), controlBase.getMatchController().getCurrentState(getNickname()), e.getError()).send(getNickname());
            return false;
        }
        //TODO
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
