package it.polimi.ingsw.model.exceptions;

import java.util.Map;

public class MatchEndedException extends Exception
{
    String msg;
    private Map<String, Integer> ranking;
    public MatchEndedException(String msg, Map<String, Integer> ranking)
    {
        this.msg = msg;
        this.ranking = ranking;
    }

    public Map<String, Integer> getRanking() {return ranking;}
    public String getMsg(){ return msg; }
}
