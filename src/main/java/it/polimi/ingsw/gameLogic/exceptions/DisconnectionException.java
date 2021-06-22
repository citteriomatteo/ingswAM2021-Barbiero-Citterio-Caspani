package it.polimi.ingsw.gameLogic.exceptions;

/**
 * Class implementation of DisconnectionException.
 * This exception is thrown whenever the client cannot reach the server for some reasons, and the throw corresponds
 * to a View behaviour to notify the player to try connecting later, because the server is no longer working.
 */
public class DisconnectionException extends RuntimeException{
    private final boolean voluntary;

    /**
     * Simple constructor.
     * @param message the error message
     */
    public DisconnectionException(String message) {
        super(message);
        voluntary = false;
    }

    /**
     * Complex constructor.
     * @param message the error message
     * @param voluntary a 'voluntary' flag
     */
    public DisconnectionException(String message, boolean voluntary) {
        super(message);
        this.voluntary = voluntary;
    }

    /**
     * Getter
     * @return voluntary value
     */
    public boolean isVoluntary() {
        return voluntary;
    }
}
