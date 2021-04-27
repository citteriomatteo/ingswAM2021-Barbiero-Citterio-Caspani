package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.SingleMatchController;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.network.server.ServerUtilities.*;
import static java.lang.Integer.parseInt;

/**
 * This class manages the direct talk with the player, every exchanged message between client
 * and server has to pass from here
 */
public class PlayersHandler implements Runnable {
    private Socket socket;
    private Player player;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Generate a PlayerHandler
     * @param socket the socket for communication with the player
     */
    public PlayersHandler(Socket socket) {
        this.socket = socket;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Used for restoring connection with a disconnected player previously linked to this handler.
     * If the player wasn't previously disconnected the socket isn't substituted.
     * @param socket the new socket created for the new connection with this player
     * @return true if the socket has been correctly substituted,
     *         false if the player is still connected before the call of this method
     */
    public boolean setSocket(Socket socket) {
        if (!player.isConnected()) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.connect();
            return true;
        }
        return false;
    }


    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String readLine;

            out.println("Welcome to Masters of Renaissance!\nEnter your nickname: ");
            readLine = in.readLine();
            //TODO: controls on existing nickname and previous players disconnection

            player = new Player(readLine);
            //add the player in the global register
            addNewPlayer(this);

            do {
                out.println("Would you like to play a single match? [y/n]");
                readLine = in.readLine();
            }while(!(readLine.equals("y") || readLine.equals(("n"))));

            if(readLine.equals("y"))
            {
                //TODO: ACCEPT CONFIGURATION FROM THE PLAYER, CREATE THE SINGLEMATCH AND START A SINGLECONTROLLER THREAD.
                System.out.println("Player: "+ player.getNickname() + " chose to play a single-player match");
                new SingleMatchController(player).start();
            }

            else  //%%%%%%%%%%%%%%% MULTIPLAYER %%%%%%%%%%%%%%%%%%%
                {
                    System.out.println("Player: "+ player.getNickname() + " chose to play a multiplayer match");

                    if(isThereAPendentMatch()) {
                        out.println("Participating to an existing match...");
                        participateToCurrentMatch(player);
                    }
                    else {
                        int numPlayers = 0;
                        do {
                            out.println("You are the first player, select how many player you want in your match [2/3/4]");
                            try{
                                numPlayers = parseInt(in.readLine());
                            }catch (NumberFormatException ignored){
                            }
                        }while(numPlayers<2 || numPlayers>4);

                        searchingForPlayers(player, numPlayers);
                        System.out.println(player.getNickname() + " is searching for " + numPlayers + " players...");
                        List<Player> playersInMatch = matchParticipants();
                        System.out.println(playersInMatch);

                    }
                    //TODO: MULTI MATCH IMPLEMENTATION.
                }


            //Build the parser for json input message
            Gson g = cToSMessageConfig(new GsonBuilder()).setPrettyPrinting().create();
            Message msg;

            //TODO: change condition for exiting while cycle
            while (!readLine.equals("exit")) {
                out.println("Write a message object in json format or \"exit\" to close the connection"); //this part will be deleted
                readLine = in.readLine();
                if (readLine.equals("ping")) {
                    out.println("pong");
                }else
                {
                    try{
                        msg = g.fromJson(readLine, Message.class);
                    }

                    catch (JsonSyntaxException e){
                        if (!readLine.equals("exit"))
                            out.println("Wrong json syntax");
                        continue;
                    }
                    System.out.println("Received: " + msg);
                }
            }


            // Close stream and socket, this doesn't close the connection on the client side.
            // The client continues thinking the connection with the server is still open
            // until he tries to send something, in that case the client terminates his execution.
            in.close();
            out.close();
            socket.close();
            System.out.println("Closed connection with " + player.getNickname());
            removePlayer(this);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

