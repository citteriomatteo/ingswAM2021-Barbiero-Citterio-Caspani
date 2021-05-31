package it.polimi.ingsw.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Class used for reading from a BufferedReader managing and filtering ping messages
 */
public class TimeoutBufferedReader extends BufferedReader {
    private static final int WAIT_FOR_PING = 60000; //1 minute

    /**
     * Private constructor that copy the {@link BufferedReader} one.
     * To make a TimeoutBufferedReader utilize {@link TimeoutBufferedReader#getNewTimeoutBufferedReader(Socket)}
     * @param in a reader
     */
    private TimeoutBufferedReader(@org.jetbrains.annotations.NotNull Reader in) {
        super(in);
    }

    /**
     * Creates and returns a new TimeoutBufferedReader linked to the socket, setting a fixed timeout
     * @param socket the socket you want to communicate through
     * @return the new TimeoutBufferedReader
     * @throws IOException if an I/O error occurs when creating the input stream, the socket is closed,
     *                     the socket is not connected, or the socket input has been shutdown using
     *                     shutdownInput()
     */
    public static TimeoutBufferedReader getNewTimeoutBufferedReader(Socket socket) throws IOException {
        try {
            socket.setSoTimeout(WAIT_FOR_PING);
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("error in TCP connection");
            System.exit(1);
        }
        return new TimeoutBufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public String readLine() throws IOException {
        String arrived;
        try{
            do{
                arrived = super.readLine();
            }while("ping".equals(arrived));
            return arrived;
        }
        catch (SocketTimeoutException t) {
            return null;  //null is read only if the client is disconnected
        }
    }
}
