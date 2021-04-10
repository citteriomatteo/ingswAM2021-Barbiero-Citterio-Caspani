package it.polimi.ingsw.model.exceptions;

public class HighCardLevelException extends InvalidOperationException
{
    public HighCardLevelException(String error)
    {
        super(error);
    }
}