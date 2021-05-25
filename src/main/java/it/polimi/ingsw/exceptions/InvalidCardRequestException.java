package it.polimi.ingsw.exceptions;

public class InvalidCardRequestException extends InvalidOperationException
{
    public InvalidCardRequestException(String error)
    {
        super(error);
    }

}