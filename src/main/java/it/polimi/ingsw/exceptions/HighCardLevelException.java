package it.polimi.ingsw.exceptions;

public class HighCardLevelException extends InvalidOperationException
{
    public HighCardLevelException(String error)
    {
        super(error);
    }
}