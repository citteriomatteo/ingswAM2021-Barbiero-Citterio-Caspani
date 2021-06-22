package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles every problem about warehouse shelves inserts (adds, switches, etc.).
 */
public class ShelfInsertException extends InvalidOperationException
{
    /**
     * Constructor
     * @param error the error message
     */
    public ShelfInsertException(String error) {
        super(error);
    }

}
