package it.polimi.ingsw.exceptions;

/**
 * This exception is a top-level exception: multiple other exception types cause a RetryException throw.
 * When this exception is caught, a retry message is sent to the player and the controller, depending on the game phase,
 * reacts restoring the situation before the bad move.
 */
public class RetryException extends Exception{

    /**
     * Constructor
     * @param error the error message
     */
    public RetryException(String error) {
        super(error);
    }
}
