package it.polimi.ingsw.model.exceptions;

public class InvalidCardRequestException extends InvalidOperationException
{
    public InvalidCardRequestException(String error)
    {
        super(error);
    }

    public String getError() {return error;}
}