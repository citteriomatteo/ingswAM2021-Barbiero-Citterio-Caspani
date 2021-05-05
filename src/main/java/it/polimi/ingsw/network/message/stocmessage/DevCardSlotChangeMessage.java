package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that a server sends to every client,
 * indicating the new card that a certain player has obtained.
 * It implies a VIEW change for every player.
 * Message structure: { nickname, column, ID of the card }
 */

public class DevCardSlotChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DEV_CARD_SLOT_CHANGE;
    private final int column;
    private final int cardID;

    public DevCardSlotChangeMessage(String nickname, int column, int cardID){
        super(nickname);
        this.column = column;
        this.cardID = cardID;
    }

    public StoCMessageType getType(){
        return type;
    }
    public int getColumn(){ return column; }
    public int getCardID(){ return cardID; }

}
