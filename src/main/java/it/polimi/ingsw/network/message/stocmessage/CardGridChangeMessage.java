package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;

import java.util.List;

/**
 * This class implements the message that the server sends to every client on a card grid move.
 * Message structure: { nickname, num of row, num of column, ID of the new surfaced card }
 */

public class CardGridChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.CARD_GRID_CHANGE;
    private final List<String>[][] newCardGrid;

    public CardGridChangeMessage(String nickname, List<String>[][] newCardGrid){
        super(nickname);
        this.newCardGrid = newCardGrid;
    }

    @Override
    public boolean compute(Client client) {
        client.getController().getMatch().setCardGrid(newCardGrid);
        return true;
    }

    public StoCMessageType getType(){
        return type;
    }

    public List<String>[][] getNewCardGrid() {
        return newCardGrid;
    }
}
