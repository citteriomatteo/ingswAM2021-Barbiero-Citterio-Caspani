package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;

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
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
        System.out.println("Server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("new client accepted");
                executor.submit(new PlayersHandler(socket));
            }
            catch(IOException e) {
                 break;  //entrerei qui se serverSocket venisse chiuso
            }
         }
             executor.shutdown();
    }
    public static void main(String[] args) {
        Server echoServer = new Server(1337);
        echoServer.startServer();
    }
}
