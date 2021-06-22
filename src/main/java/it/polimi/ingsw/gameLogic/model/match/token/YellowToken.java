package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.model.essentials.CardColor;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.SingleCardGrid;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;

/**
 * This class implements the yellow token.
 */
public class YellowToken extends Token{

    /**
     * This method call the discard method of singleCardGrid to discard two green developmentCards
     * @param match the actual singleMatch
     * @return true if the discard method worked
     * @throws LastRoundException if the number of yellow card in singleCardGrid became 0
     */
    @Override
    public boolean onDraw(SingleMatch match) throws LastRoundException {
        SingleCardGrid singleCardGrid = (SingleCardGrid) match.getCardGrid();
        boolean res = singleCardGrid.discard(CardColor.YELLOW);

        updateCardGrid(match);

        return res;
    }

    @Override
    public String toString() {
        return "YellowToken";
    }
}
