package it.polimi.ingsw.exceptions;

public class InvalidOperationException extends Exception
{
    String error;
    public InvalidOperationException(String error) { this.error=error; }

    public String getError() {return error;}
}
