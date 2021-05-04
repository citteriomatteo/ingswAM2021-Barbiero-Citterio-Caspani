package it.polimi.ingsw.exceptions;

public class FullColumnException extends InvalidOperationException
{
    public FullColumnException(String error)
    {
        super(error);
    }
}

