package it.polimi.ingsw.exceptions;

public class DisconnectionException extends RuntimeException{
    private final boolean voluntary;
    public DisconnectionException(String message) {
        super(message);
        voluntary = false;
    }

    public DisconnectionException(String message, boolean voluntary) {
        super(message);
        this.voluntary = voluntary;
    }

    public boolean isVoluntary() {
        return voluntary;
    }
}
