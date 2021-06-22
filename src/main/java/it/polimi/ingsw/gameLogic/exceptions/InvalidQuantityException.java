package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles all the client input errors in quantities: not existing card levels, warehouse insertions
 * not present in the market buffer, etc.
 */
public class InvalidQuantityException extends RuntimeException {

    /**
     * Constructor
     * @param error the error message
     */
    public InvalidQuantityException(String error) {
        super(error);
    }
}