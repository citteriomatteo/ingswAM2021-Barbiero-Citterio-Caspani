package it.polimi.ingsw.model.exceptions;

public class FaithPathCreationException extends WrongSettingException
{

    public FaithPathCreationException(String error) { super(error); }

    public String getError() {return super.getError(); }
}
