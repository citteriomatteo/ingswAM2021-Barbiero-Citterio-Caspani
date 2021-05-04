package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;

public class PlusTwoToken extends Token{

    /**
     * This method adds two blackFaithPoint to the singleFaithPath
     * @param match the actual singleMatch
     * @return true if the method worked
     * @throws MatchEndedException if Lorenzo reaches the end of his faithPath
     */
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {
        SingleFaithPath singleFaithPath;
        singleFaithPath = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        return singleFaithPath.addBlackPoints(2);
    }
}
