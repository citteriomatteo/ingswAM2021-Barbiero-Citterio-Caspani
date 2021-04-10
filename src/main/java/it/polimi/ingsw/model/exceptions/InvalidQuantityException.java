package it.polimi.ingsw.model.exceptions;

public class InvalidQuantityException extends Exception {
    private String error;

    public InvalidQuantityException(String error) {
        this.error = error;
    }

    public String getError() {return error;}
}