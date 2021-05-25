package it.polimi.ingsw.exceptions;

public class ShelfInsertException extends InvalidOperationException
{
    public ShelfInsertException(String error) {
        super(error);
    }

}
