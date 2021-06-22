package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles: bad choices of card or slot indexes inside the card grid, dev card slots, etc.
 */
public class InvalidCardRequestException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public InvalidCardRequestException(String error)
    {
        super(error);
    }

}