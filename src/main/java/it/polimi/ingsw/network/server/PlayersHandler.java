package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.SingleMatchController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayersHandler implements Runnable {
    public static BlockingQueue<Socket> players = new LinkedBlockingQueue<>();
    private final Socket socket;

    public PlayersHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream()); // leggo e scrivo nella connessione finche' non ricevo "quit"
            while(true){
                out.println("Welcome to Masters of Renaissance!\nWould you like to play a single match? [y/n]");
                String line = in.nextLine();
                if(line.equals("y"))
                {
                    out.println("Enter your nickname: ");
                    String nickname = in.nextLine();
                    new SingleMatchController(nickname).start();

                }

                else
                    if(line.equals("n"))
                    {
                        //TODO: MULTI MATCH IMPLEMENTATION.

                    }

            }

            /*while (true) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    out.println("Received: " + line);
                    out.flush();
                }
            } // chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();*/
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

