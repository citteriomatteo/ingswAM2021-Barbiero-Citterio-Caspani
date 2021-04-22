package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that a server sends to every client,
 * indicating the new card that a certain player has obtained.
 * It implies a VIEW change for every player.
 * Message structure: { nickname, column, ID of the card }
 */

public class DevCardSlotChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.DEV_CARD_SLOT_CHANGE;
    private String nickname;
    private int column;
    private int cardID;

    public DevCardSlotChangeMessage(String receiver, String nickname, int column, int cardID){
        super(receiver);
        this.nickname = nickname;
        this.column = column;
        this.cardID = cardID;
    }

    private StoCMessageType getType(){
        return type;
    }
    private String getNickname(){ return nickname; }
    private int getColumn(){ return column; }
    private int getCardID(){ return cardID; }

}
