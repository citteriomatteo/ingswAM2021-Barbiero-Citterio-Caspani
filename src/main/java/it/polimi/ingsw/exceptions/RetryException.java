package it.polimi.ingsw.exceptions;

public class RetryException extends Exception{

    public RetryException(String error) {
        super(error);
    }
}
