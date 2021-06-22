package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception is thrown when a disconnected tries to get back to the match he was into.
 * It causes the launching of the reconnection procedure.
 */
public class ReconnectionException extends Exception{
    /**
     * Constructor
     * @param message the reconnection info message
     */
    public ReconnectionException(String message) {
        super(message);
    }
}
