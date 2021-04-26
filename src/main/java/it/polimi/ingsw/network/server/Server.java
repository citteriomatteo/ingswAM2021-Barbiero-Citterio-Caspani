package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

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
                PlayersHandler player = new PlayersHandler(socket);
                executor.submit(player);
            }
            catch(IOException e) {
                System.out.println("ServerSocket closed");
                 break;  //I'll enter here if ServerSocket will be closed
            }
         }
             executor.shutdown();
    }


    public static void main(String[] args) {
        Server echoServer = new Server(1337);
        echoServer.startServer();
    }
}
