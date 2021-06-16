package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;

/**
 * This abstract class is common for every token and implements the common operation( such as onDraw, updates, ...)
 */
public abstract class Token {

    /**
     * This method adds one or two backFaithPoints or discards two development cards
     * @param match the actual singleMatch
     * @return true if the method worked
     * @throws LastRoundException if Lorenzo reaches the end of his faithPath
     */
    public abstract boolean onDraw(SingleMatch match) throws LastRoundException;

    /**
     * This method is called by the four tokens that draw some cards from the grid.
     * This goes to update the card grid, in order to cause a CardGridChangeMessage creation.
     * @param match the match.
     */
    public void updateCardGrid(Match match){
        //update_call
        match.getCurrentPlayer().updateCardGrid(match.getCardGrid());
    }

    public abstract String toString();
}
