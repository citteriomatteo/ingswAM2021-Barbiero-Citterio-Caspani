package it.polimi.ingsw.exceptions;

public class RetryException extends Exception{
    private String error;

    public RetryException(String error) {
        this.error = error;
    }

    public String getError() {return error;}
}
