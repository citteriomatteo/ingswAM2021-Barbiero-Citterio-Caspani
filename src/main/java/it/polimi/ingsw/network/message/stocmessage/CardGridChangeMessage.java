package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements the message that the server sends to every client on a card grid move.
 * Message structure: { nickname, num of row, num of column, ID of the new surfaced card }
 */

public class CardGridChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.CARD_GRID_CHANGE;
    private final int row, column;
    private final int cardID;

    public CardGridChangeMessage(String nickname, int row, int column, int cardID){
        super(nickname);
        this.row = row;
        this.column = column;
        this.cardID = cardID;
    }

    public StoCMessageType getType(){
        return type;
    }
    public int getRow(){ return row; }
    public int getColumn(){ return column; }
    public int getCardID(){
        return cardID;
    }

}
