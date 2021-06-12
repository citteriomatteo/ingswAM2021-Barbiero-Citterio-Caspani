package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown whenever a player or Lorenzo satisfy end match requirements.
 * It causes a last round message to be sent in broadcast, to inform every player in match that this round will be the last.
 */
public class LastRoundException extends Exception
{
    /**
     * Constructor
     * @param msg the info message
     */
    public LastRoundException(String msg){
        super(msg);
    }
}
