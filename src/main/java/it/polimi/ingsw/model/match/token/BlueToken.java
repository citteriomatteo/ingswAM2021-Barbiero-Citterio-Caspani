package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleCardGrid;
import it.polimi.ingsw.model.match.SingleMatch;

public class BlueToken extends Token{

    /**
     * This method call the discard method of singleCardGrid to discard two blu developmentCards
     * @param match the actual singleMatch
     * @return true if the discard method worked
     * @throws MatchEndedException if the number of blue card in singleCardGrid became 0
     */
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {

        SingleCardGrid singleCardGrid = (SingleCardGrid) match.getCardGrid();
        return singleCardGrid.discard(CardColor.BLUE);

    }
}
