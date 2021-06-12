package it.polimi.ingsw.exceptions;

/**
 * This exception is the older version of the MatchRestartException.
 * @see MatchRestartException
 */
public class RestartException extends Exception{

    /**
     * Constructor
     * @param msg the message
     */
    public RestartException(String msg){
        super(msg);
    }
}
