package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleCardGrid;
import it.polimi.ingsw.model.match.SingleMatch;

public class YellowToken extends Token{
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {
        SingleCardGrid singleCardGrid = (SingleCardGrid) match.getCardGrid();
        return singleCardGrid.discard(CardColor.YELLOW);
    }
}
