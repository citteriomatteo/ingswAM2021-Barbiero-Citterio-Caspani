package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles inserts of cards in a non-valid slot, due to a lower level card miss on position before of the same slot.
 */
public class HighCardLevelException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public HighCardLevelException(String error)
    {
        super(error);
    }
}