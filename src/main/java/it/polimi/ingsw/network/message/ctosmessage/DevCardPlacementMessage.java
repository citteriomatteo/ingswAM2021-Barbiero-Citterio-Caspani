package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the player to order to place the development card(identified by its Id)
 * in the selected column of the devCardSlots
 * Message structure: { nickname, row's number, column's number }
 */

public class DevCardPlacementMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.DEV_CARD_PLACEMENT;
    private final String devCardId;
    private final int column;

    public DevCardPlacementMessage(String nickname, String devCardId, int column) {
        super(nickname);
        this.devCardId = devCardId;
        this.column = column;
    }

    /**
     * Getter
     * @return the card's Id
     */
    public String getDevCardId() {
        return devCardId;
    }

    /**
     * Getter
     * @return the number of the devCardSlots' column in which insert the card
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
