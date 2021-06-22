package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exception is thrown by the rematch controller when every player accepted the rematch.
 */
public class MatchRestartException extends Exception {

    /**
     * Constructor
     * @param msg the info message
     */
    public MatchRestartException(String msg){
        super(msg);
    }
}
