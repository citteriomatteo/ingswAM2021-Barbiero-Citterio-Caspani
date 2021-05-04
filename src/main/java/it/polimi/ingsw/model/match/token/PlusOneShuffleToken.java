package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;

public class PlusOneShuffleToken extends Token{

    /**
     * This method adds one blackFaithPoint to the singleFaithPath and recreate a new shuffled tokenStack
     * @param match the actual singleMatch
     * @return true if all worked
     * @throws MatchEndedException if Lorenzo reaches the end of his faithPath
     */
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {
        SingleFaithPath singleFaithPath;
        singleFaithPath = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();

        return (singleFaithPath.addBlackPoints(1) && match.getTokenStack().shuffle());
    }
}
