package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;

/**
 * This class implements the plus two token.
 */
public class PlusTwoToken extends Token{

    /**
     * This method adds two blackFaithPoint to the singleFaithPath
     * @param match the actual singleMatch
     * @return true if the method worked
     * @throws LastRoundException if Lorenzo reaches the end of his faithPath
     */
    @Override
    public boolean onDraw(SingleMatch match) throws LastRoundException {
        return match.getCurrentPlayer().addBlackPoints(2);
    }

    @Override
    public String toString() {
        return "PlusTwoToken";
    }
}
