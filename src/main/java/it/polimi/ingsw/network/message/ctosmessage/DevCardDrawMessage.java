package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the player to order to draw the development card in the selected row and column
 * from the cardGrid
 * Message structure: { nickname, row's number, column's number }
 */
public class DevCardDrawMessage extends Message {
    private final CtoSMessageType type = CtoSMessageType.DEV_CARD_DRAW;
    private final int row;
    private final int column;

    public DevCardDrawMessage(String nickname, int row, int column) {
        super(nickname);
        this.row = row;
        this.column = column;
    }

    /**
     * Getter
     * @return the row's number
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter
     * @return the column's number
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
