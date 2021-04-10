package it.polimi.ingsw.model.exceptions;

public class ShelfInsertException extends Exception
{
    private String error;
    public ShelfInsertException(String error) { this.error=error; }

    public String getError() {return error;}
}
