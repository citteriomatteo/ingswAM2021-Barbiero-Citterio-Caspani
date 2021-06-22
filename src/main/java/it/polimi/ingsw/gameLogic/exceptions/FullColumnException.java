package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles invalid dev card placements inside a full slot.
 */
public class FullColumnException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public FullColumnException(String error)
    {
        super(error);
    }
}

