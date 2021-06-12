package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

import java.util.List;

/**
 * This class implements a message that a server sends to every client,
 * indicating the new card that a certain player has obtained.
 * It implies a VIEW change for every player.
 * Message structure: { nickname, column, ID of the card }
 */

public class DevCardSlotChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.DEV_CARD_SLOT_CHANGE;
    private final List<String>[] newDevSlots;

    /**
     * Constructor of a dev card slot update message.
     * @param nickname the receiver
     * @param newDevSlots the new dev card slots
     */
    public DevCardSlotChangeMessage(String nickname, List<String>[] newDevSlots){
        super(nickname);
        this.newDevSlots = newDevSlots;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setDevCardSlots(getNickname(),newDevSlots);
        return true;
    }

    @Override
    public StoCMessageType getType(){
        return type;
    }

    /**
     * Getter
     * @return the new dev card slots
     */
    public List<String>[] getNewDevSlots() {
        return newDevSlots;
    }
}
