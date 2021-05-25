package it.polimi.ingsw.exceptions;

public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(String error) {
        super(error);
    }
}