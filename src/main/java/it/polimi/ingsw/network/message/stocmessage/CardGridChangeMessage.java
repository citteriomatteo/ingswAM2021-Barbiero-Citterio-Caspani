package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

import java.util.List;

/**
 * This class implements the message that the server sends to every client on a card grid move.
 * Message structure: { nickname, num of row, num of column, ID of the new surfaced card }
 */
public class CardGridChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.CARD_GRID_CHANGE;
    private final List<String>[][] newCardGrid;

    /**
     * Constructor of a card grid change's message.
     * @param nickname the nickname
     * @param newCardGrid the new version of the card grid
     */
    public CardGridChangeMessage(String nickname, List<String>[][] newCardGrid){
        super(nickname);
        this.newCardGrid = newCardGrid;
    }

    @Override
    public boolean compute(Client client) {
        client.getController().getMatch().setCardGrid(newCardGrid);
        return true;
    }

    @Override
    public StoCMessageType getType(){
        return type;
    }
    /**
     * Getter
     * @return the new summarized card grid
     */
    public List<String>[][] getNewCardGrid() {
        return newCardGrid;
    }
}
