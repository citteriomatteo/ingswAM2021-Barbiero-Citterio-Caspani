package it.polimi.ingsw.exceptions;

public class InvalidQuantityException extends RuntimeException {
    private String error;

    public InvalidQuantityException(String error) {
        this.error = error;
    }

    public String getError() {return error;}
}