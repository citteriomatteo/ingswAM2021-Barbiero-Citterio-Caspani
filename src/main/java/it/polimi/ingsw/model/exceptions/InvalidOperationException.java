package it.polimi.ingsw.model.exceptions;

public class InvalidOperationException extends Exception
{
    String error;
    public InvalidOperationException(String error)
    {
        this.error=error;
    }
}
