package it.polimi.ingsw.exceptions;

/**
 * This exception handles all customized configuration creations issues.
 */
public class WrongSettingException extends Exception
{
    /**
     * Constructor
     * @param error the error message
     */
    public WrongSettingException(String error)
    {
        super(error);
    }
}