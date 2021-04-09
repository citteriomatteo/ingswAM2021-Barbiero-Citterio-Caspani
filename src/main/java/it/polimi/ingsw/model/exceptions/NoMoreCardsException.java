package it.polimi.ingsw.model.exceptions;

public class NoMoreCardsException extends InvalidOperationException
{
    public NoMoreCardsException(String error)
    {
        super(error);
    }
}