package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.gameLogic.exceptions.RetryException;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the client to call a draw action from the market
 * Message structure: { nickname, true if the player selected a row (false if he selected a column),
 * the number of the row/column selected}
 */
public class MarketDrawMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.MARKET_DRAW;
    private final boolean row;
    private final int num;

    /**
     * Constructor of a market draw message.
     * @param nickname the sender
     * @param row a row/column boolean
     * @param num the number of the row/column
     */
    public MarketDrawMessage(String nickname, boolean row, int num) {
        super(nickname);
        this.row = row;
        this.num = num;
    }

    /**
     * Getter
     * @return true if the player selected a row, false if he selected a column
     */
    public boolean isRow() {
        return row;
    }

    /**
     * Getter
     * @return the the number of the row/column selected
     */
    public int getNum() {
        return num;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if (isSomethingNull()) {
            sendRetryMessage(getNickname(), controlBase, "You forgot some parameters");
            return false;
        }
        try {
            return controlBase.getMatchController().marketDraw(getNickname(), row, num);
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
