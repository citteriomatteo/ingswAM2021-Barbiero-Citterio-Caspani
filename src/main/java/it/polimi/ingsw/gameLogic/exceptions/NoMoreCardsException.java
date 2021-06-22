package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles problems about using/trying to buy cards out of an empty stack.
 */
public class NoMoreCardsException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public NoMoreCardsException(String error)
    {
        super(error);
    }
}