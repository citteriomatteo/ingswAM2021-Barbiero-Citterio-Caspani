package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.market.Marble;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the message that the server sends to every client in the match
 * when someone makes a deal on the market.
 * Message structure: { nickname, side marble, row/column changed, row/column boolean, chosen row/column }
 */

public class MarketChangeMessage extends Message {

    private final StoCMessageType type = StoCMessageType.MARKET_CHANGE;
    private Marble sideMarble;
    private List<Marble> changedSection;
    private boolean row;
    private int sectionIndex;

    public MarketChangeMessage(String nickname, Marble sideMarble, List<Marble> changedSection, boolean row, int sectionIndex)
    {
        super(nickname);
        this.sideMarble = sideMarble;
        this.changedSection = new ArrayList<>(changedSection);
        this.row = row;
        this.sectionIndex = sectionIndex;
    }

}
