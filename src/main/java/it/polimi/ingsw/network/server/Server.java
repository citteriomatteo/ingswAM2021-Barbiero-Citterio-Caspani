package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.jsonUtilities.Preferences.ReadPortFromJSON;

/**
 * This Class implements the main server, it will accept and manage every client that will try to connect
 */
public class Server {
//    private static final int TIME_FOR_PING = 10000; //10 seconds
    private final int port;

    /**
     * Creates a Server object that will accept clients on a socket bounded to the specified port
     * @param port the number of port through which the server will create his socket
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Continues accepting clients and delegates the job for handling communication with them to different threads
     * of the class {@link PlayerHandler}
     */
    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            //can't open the ServerSocket
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("new client accepted");
                PlayerHandler player = new PlayerHandler(socket);
                executor.submit(player);
            }
            catch(IOException e) {
                System.out.println("ServerSocket closed");
                 break;  //I'll enter here if ServerSocket will be closed
            }
         }
             executor.shutdown();
    }

// &&&&&&&&&&&&& First attempt for implementing ping

//    private void setConnectionController() {
//        Timer t = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("It's ping time!");
//                pingAll();
//            }
//        };
//        t.scheduleAtFixedRate(tt,6000,TIME_FOR_PING);
//    }


    public static void main(String[] args) {
        int portNumber;
        if (args.length==1)
            portNumber = Integer.parseInt(args[0]);
        else{
            portNumber = ReadPortFromJSON();
        }
        Server echoServer = new Server(portNumber);
        echoServer.startServer();
    }
}
