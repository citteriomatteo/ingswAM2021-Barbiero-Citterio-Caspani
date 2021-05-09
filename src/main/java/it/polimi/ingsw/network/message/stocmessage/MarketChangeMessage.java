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

public class MarketChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.MARKET_CHANGE;
    private final char sideMarble;
    private final List<Character> changedSection;
    private final boolean row;
    private final int sectionIndex;

    public MarketChangeMessage(String nickname, char sideMarble, List<Character> changedSection, boolean row, int sectionIndex)
    {
        super(nickname);
        this.sideMarble = sideMarble;
        this.changedSection = new ArrayList<>(changedSection);
        this.row = row;
        this.sectionIndex = sectionIndex;
    }

    public StoCMessageType getType(){ return type; }
    public Character getSideMarble(){ return sideMarble; }
    public List<Character> getChangedSection(){ return changedSection; }
    public boolean isRow(){ return row; }
    public int getSectionIndex(){ return sectionIndex; }

}
