package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.MultiMatchController;
import it.polimi.ingsw.controller.SingleMatchController;
import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.network.server.ServerUtilities.*;
import static java.lang.Integer.parseInt;

/**
 * This class manages the direct talk with the player, every exchanged message between client
 * and server has to pass from here
 */
public class PlayerHandler implements Runnable, Observer {
//    private static final int WAIT_FOR_READING = 60000; //1 minute
    private Socket socket;
    private Player player;
    private BufferedReader in;
    private PrintWriter out;
    private final AtomicBoolean pendantMessage;

    /**
     * Generate a PlayerHandler
     * @param socket the socket for communication with the player
     */
    public PlayerHandler(Socket socket) {

// &&&&&&&&&&&&& First attempt for implementing ping

//        try {
//            socket.setSoTimeout(WAIT_FOR_READING);
//        } catch (SocketException e) {
//            e.printStackTrace();
//            System.out.println("error in TCP connection");
//            System.exit(1);
//        }

        this.socket = socket;
        pendantMessage = new AtomicBoolean();
    }

    public Player getPlayer() {
        return player;
    }

// &&&&&&&&&&&&& First attempt for implementing ping

//    public synchronized boolean verifyConnection(){
//        out.println("ping");
//        try {
//            if(in.readLine() == null ) {
//                System.out.println("Player "+ player + " is unavailable");
//                player.disconnect();
//                return false;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }

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

//%%%%%%%%%%%%%%%% MAIN FUNCTION %%%%%%%%%%%%%%%%%%%%

    public void run() {
        try {
            //initialize the player and do the configuration
            init();

            //Build the parser for json input message
            Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).setPrettyPrinting().create();

            //Build the parser for json output message
            Gson parserStoC = sToCMessageConfig(new GsonBuilder()).setPrettyPrinting().create();

            Message inMsg;
            Message outMsg;

            String readLine;
            System.out.println("Player " + player + " enters the main cycle");
            boolean stop = false;
            //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            //%%%%%%%%%%%%%% MAIN CYCLE %%%%%%%%%%%%%%%
            //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            while (!stop) {
                while(pendantMessage.getAndSet(false)){
                    System.out.println("there is a new message to send at " + player);
                    outMsg = pullStoCMessage(player.getMatch());
                    parserStoC.toJson(outMsg, out);
                    System.out.println("Message sent");
                }

                out.println("Write a message object in json format or \"exit\" to close the connection"); //this part will be deleted
                readLine = in.readLine();
                if (readLine.equals("ping")) {
                    out.println("pong");
                } else {
                    try {
                        inMsg = parserCtoS.fromJson(readLine, Message.class);
                    } catch (JsonSyntaxException e) {
                        if (readLine.equals("exit"))
                            stop = true;
                        else
                            out.println("Wrong json syntax");
                        continue;
                    }
                    System.out.println("Received: " + inMsg);
                }
            }

            //close reader, writer and socket connection
            terminateConnection();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Initialization of the player, set writer and reader, talk with the client and set nickname and the correct match
     * @throws IOException if something goes wrong with the reading or writing with the player
     */
    private void init() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //consider using TimeoutBufferReader
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
        } while (!(readLine.equals("y") || readLine.equals(("n"))));

        if (readLine.equals("y")) { //%%%%%%%%%% SINGLE PLAYER %%%%%%%%%%

            System.out.println("Player: " + player + " chose to play a single-player match");
            new SingleMatchController(player).start();

        } else  //%%%%%%%%%%%%%%% MULTIPLAYER %%%%%%%%%%%%%%%%%%%
        {
            System.out.println("Player: " + player + " chose to play a multiplayer match");

            if (isThereAPendentMatch()) {
                out.println("Participating to an existing match...");
                participateToCurrentMatch(player);
            } else {
                int numPlayers = 0;
                do {
                    out.println("You are the first player, select how many player you want in your match [2/3/4]");
                    try {
                        numPlayers = parseInt(in.readLine());
                    } catch (NumberFormatException ignored) {}

                } while (numPlayers < 2 || numPlayers > 4);

                searchingForPlayers(player, numPlayers);
                System.out.println(player + " is searching for " + numPlayers + " players...");
                List<Player> playersInMatch = matchParticipants();

                System.out.println("forming a new match for... " + playersInMatch);

                try {
                    new MultiMatchController(playersInMatch).start();
                } catch (SingleMatchException e) {
                    //TODO: wrong match choose
                    e.printStackTrace();
                }


            }
            //TODO: MATCH IMPLEMENTATION.
        }
    }

    /**
     * Close stream and socket, this doesn't close the connection on the client side.
     * The client continues thinking the connection with the server is still open
     * until he tries to send something, in that case the client terminates his execution.
     * @throws IOException if an I/O error occurs
     */
    private void terminateConnection() throws IOException {
        out.println("Closing connection... Thanks for having played with us!");
        in.close();
        out.close();
        socket.close();
        System.out.println("Closed connection with " + player.getNickname());
        removePlayer(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("player " + player + " updated");
        if(arg.equals("StoC")) {
            pendantMessage.set(true);
            System.out.println("set the boolean in " + player);
        }
    }
}

