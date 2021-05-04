package it.polimi.ingsw.exceptions;

public class NoMoreCardsException extends InvalidOperationException
{
    public NoMoreCardsException(String error)
    {
        super(error);
    }

    public String getError() {return error;}
}