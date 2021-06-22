package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception handles: negative resources and faith positions quantities.
 */
public class NegativeQuantityException extends InvalidQuantityException {

    /**
     * Constructor
     * @param error the error message
     */
    public NegativeQuantityException(String error) { super(error);  }
}
