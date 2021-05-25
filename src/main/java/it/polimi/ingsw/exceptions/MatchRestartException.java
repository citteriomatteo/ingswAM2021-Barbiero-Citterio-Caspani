package it.polimi.ingsw.exceptions;

public class MatchRestartException extends Exception {

    String msg;

    public MatchRestartException(String msg){
        this.msg = msg;
    }

    public String getMsg(){ return msg; }


}
