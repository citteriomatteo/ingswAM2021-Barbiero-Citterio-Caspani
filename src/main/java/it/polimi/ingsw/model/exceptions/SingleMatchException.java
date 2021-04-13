package it.polimi.ingsw.model.exceptions;

public class SingleMatchException extends Throwable {
    private String error;
    public SingleMatchException(String error) {
        this.error = error;
    }
    public String getError() {return error; }

}
