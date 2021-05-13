package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the player to order to draw the development card in the selected row and column
 * from the cardGrid
 * Message structure: { nickname, row's number, column's number }
 */
public class DevCardDrawMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.DEV_CARD_DRAW;
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


    @Override
    public boolean computeMessage(ControlBase controlBase){
        if (isSomethingNull()){
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().devCardDraw(getNickname(), row, column);
        } catch (RetryException e) {
            sendRetryMessage(getNickname(), controlBase, e.getError());
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
