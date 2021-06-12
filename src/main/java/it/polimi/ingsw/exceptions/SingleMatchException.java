package it.polimi.ingsw.exceptions;

/**
 * This exception handles a rare and remote problem that consists in creating a multiMatch with just one player partecipating.
 */
public class SingleMatchException extends Throwable {

    /**
     * Constructor
     * @param error the error message
     */
    public SingleMatchException(String error) {
        super(error);
    }
}
