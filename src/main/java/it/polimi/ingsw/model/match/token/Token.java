package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;

public abstract class Token {

    /**
     * This method adds one or two backFaithPoints or discards two development cards
     * @param match the actual singleMatch
     * @return true if the method worked
     * @throws MatchEndedException if Lorenzo reaches the end of his faithPath
     */
    public abstract boolean onDraw(SingleMatch match) throws MatchEndedException;
}
