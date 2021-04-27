package it.polimi.ingsw.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.SocketTimeoutException;


// &&&&&&&&&&&&& First attempt for implementing ping
public class TimeoutBufferedReader extends BufferedReader {

    public TimeoutBufferedReader(@org.jetbrains.annotations.NotNull Reader in, int sz) {
        super(in, sz);
    }

    public TimeoutBufferedReader(@org.jetbrains.annotations.NotNull Reader in) {
        super(in);
    }

    @Override
    public String readLine(){
        try{
            return super.readLine();
        }
        catch (SocketTimeoutException t){
            return "timeout";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "timeout";
    }
}
