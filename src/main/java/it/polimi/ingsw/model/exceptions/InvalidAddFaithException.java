package it.polimi.ingsw.model.exceptions;

public class InvalidAddFaithException extends Exception {
    private String error;

    public InvalidAddFaithException() {
        this.error = "";
    }
}
