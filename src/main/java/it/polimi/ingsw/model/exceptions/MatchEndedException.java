package it.polimi.ingsw.model.exceptions;

public class MatchEndedException extends Exception
{
    private String msg;
    public MatchEndedException(String msg)
    {
        this.msg = msg;
    }

    public String getMessage() {return msg;}
}
