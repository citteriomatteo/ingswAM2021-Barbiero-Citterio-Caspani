package it.polimi.ingsw.model.exceptions;

public class ShelfInsertException extends InvalidOperationException
{
    private String error;
    public ShelfInsertException(String error) {
        super(error);
        this.error=error; }

    public String getError() {return error;}
}
