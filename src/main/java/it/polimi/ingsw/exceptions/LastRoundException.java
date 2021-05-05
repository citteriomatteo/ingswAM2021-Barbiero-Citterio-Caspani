package it.polimi.ingsw.exceptions;

public class LastRoundException extends Exception
{
    String msg;

    public LastRoundException(String msg){
        this.msg = msg;
    }

    public String getMsg(){ return msg; }
}
