package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;

public class PlusOneShuffleToken extends Token{
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {
        SingleFaithPath singleFaithPath;
        singleFaithPath = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        singleFaithPath.addBlackPoints(1);
        match.getTokenStack().shuffle();

       return true;
    }
}
