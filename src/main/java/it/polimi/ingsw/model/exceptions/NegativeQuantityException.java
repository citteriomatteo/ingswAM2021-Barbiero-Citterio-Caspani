package it.polimi.ingsw.model.exceptions;

public class NegativeQuantityException extends Exception {
    private String error;

    public NegativeQuantityException(String error)
    {
        this.error=error;
    }


}
