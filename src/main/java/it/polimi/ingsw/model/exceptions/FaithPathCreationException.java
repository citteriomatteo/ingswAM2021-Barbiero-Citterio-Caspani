package it.polimi.ingsw.model.exceptions;

/**
 * This exceptions handles: invalid faith path starting point, negative quantity of cell winpoints.
 */
public class FaithPathCreationException extends WrongSettingException
{

    public FaithPathCreationException(String error) { super(error); }

    public String getError() {return super.getError(); }
}
