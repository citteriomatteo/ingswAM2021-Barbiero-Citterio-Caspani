package it.polimi.ingsw.exceptions;

import java.util.Map;

public class MatchEndedException extends Throwable {

    private final Map<String, Integer> ranking;
    public MatchEndedException(String msg, Map<String, Integer> ranking)
    {
        super(msg);
        this.ranking = ranking;
    }

    public Map<String, Integer> getRanking() {return ranking;}

}
