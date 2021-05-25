package it.polimi.ingsw.exceptions;

public class WrongSettingException extends Exception
{
    public WrongSettingException(String error)
    {
        super(error);
    }
}