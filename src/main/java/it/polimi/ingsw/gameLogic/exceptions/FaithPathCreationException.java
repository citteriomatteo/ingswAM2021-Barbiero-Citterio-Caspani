package it.polimi.ingsw.gameLogic.exceptions;

/**
 * This exceptions handles: invalid faith path starting point, negative quantity of cell winPoints.
 * Implemented in order to allow the player to build a correct customized faith path.
 */
public class FaithPathCreationException extends WrongSettingException
{
    /**
     * Constructor
     * @param error the error message
     */
    public FaithPathCreationException(String error) { super(error); }

}
