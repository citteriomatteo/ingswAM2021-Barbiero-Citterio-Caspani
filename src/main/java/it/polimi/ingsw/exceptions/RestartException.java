package it.polimi.ingsw.exceptions;

public class RestartException extends Exception{

    private String msg;

    public RestartException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
