package it.polimi.ingsw.gameLogic.exceptions;

import java.util.Map;

/**
 * This exception is thrown when the last round finishes or Lorenzo satisfies certain single-match win requirements.
 * It causes the end of the turns phase and the beginning of the rematch one.
 */
public class MatchEndedException extends Throwable {

    private final Map<String, Integer> ranking;

    /**
     * Constructor
     * @param msg the end match message
     * @param ranking the final match leaderboard map (not sorted yet).
     */
    public MatchEndedException(String msg, Map<String, Integer> ranking)
    {
        super(msg);
        this.ranking = ranking;
    }

    /**
     * Getter
     * @return the leaderboard
     */
    public Map<String, Integer> getRanking() {return ranking;}

}
