package it.polimi.ingsw.model.exceptions;

public class FullColumnException extends InvalidOperationException
{
    public FullColumnException(String error)
    {
        super(error);
    }
}

