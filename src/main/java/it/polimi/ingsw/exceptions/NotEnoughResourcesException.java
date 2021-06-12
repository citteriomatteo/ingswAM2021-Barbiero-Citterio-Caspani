package it.polimi.ingsw.exceptions;

/**
 * This exception handles invalid payments from strongbox and/or warehouse.
 */
public class NotEnoughResourcesException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public NotEnoughResourcesException (String error) {
        super(error);
    }
}
