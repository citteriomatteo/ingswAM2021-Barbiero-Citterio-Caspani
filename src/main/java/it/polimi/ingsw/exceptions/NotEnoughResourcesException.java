package it.polimi.ingsw.exceptions;

public class NotEnoughResourcesException extends InvalidOperationException
{
    public NotEnoughResourcesException (String err) {
        super(err);
    }
}
