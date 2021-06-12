package it.polimi.ingsw.exceptions;

/**
 * This exceptions handles all the bad input choices in standard in-game operation.
 * It is the top-level exception for these types of errors and causes a RetryException to be thrown.
 */
public class InvalidOperationException extends Exception
{
    /**
     * Constructor
     * @param error the error message
     */
    public InvalidOperationException(String error) { super(error); }
}
