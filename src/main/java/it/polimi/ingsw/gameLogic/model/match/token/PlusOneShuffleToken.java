package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;

/**
 * This class implements the plus one shuffle token.
 */
public class PlusOneShuffleToken extends Token{

    /**
     * This method adds one blackFaithPoint to the singleFaithPath and recreate a new shuffled tokenStack
     * @param match the actual singleMatch
     * @return true if all worked
     * @throws LastRoundException if Lorenzo reaches the end of his faithPath
     */
    @Override
    public boolean onDraw(SingleMatch match) throws LastRoundException {
        return (match.getCurrentPlayer().addBlackPoints(1) && match.getTokenStack().shuffle());
    }

    @Override
    public String toString() {
        return "PlusOneShuffleToken";
    }
}
