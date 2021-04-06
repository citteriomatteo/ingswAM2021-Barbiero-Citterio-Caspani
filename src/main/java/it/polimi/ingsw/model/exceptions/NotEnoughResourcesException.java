package it.polimi.ingsw.model.exceptions;

public class NotEnoughResourcesException extends Exception
{
    private String err;
    public NotEnoughResourcesException (String err)
    {
        this.err=err;
    }
}
