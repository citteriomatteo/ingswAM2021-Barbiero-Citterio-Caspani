package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.InitController;
import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.network.server.ServerUtilities.*;

/**
 * This class manages the direct talk with the player, every exchanged message between client
 * and server has to pass from here
 */
public class PlayerHandler implements Runnable, ControlBase{
//    private static final int WAIT_FOR_READING = 60000; //1 minute

    //Build the parser for json input message
    private static final Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).setPrettyPrinting().create();
    //Build the parser for json output message
    private static final Gson parserStoC = sToCMessageConfig(new GsonBuilder()).setPrettyPrinting().create();
    private Socket socket;
    private Player player;
    private BufferedReader in;
    private PrintWriter out;
    private MatchController matchController;
    private InitController initController;
    private final AtomicBoolean inMatch;

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
        inMatch = new AtomicBoolean(false);
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //consider using TimeoutBufferReader
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e) {
            terminateConnection(false);
            System.out.println("A problem occurs when trying to connect with a player");
        }
    }

    @Override
    public MatchController getMatchController() {
        return matchController;
    }
    @Override
    public InitController getInitController() {
        return initController;
    }
    @Override
    public Player getPlayer() {
        return player;
    }
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }
    @Override
    public void setMatchController(MatchController matchController) {
        System.out.println("set the matchController for " + player);
        this.matchController = matchController;
        inMatch.set(true);
    }


    /**
     * Used for restoring connection with a disconnected player previously linked to this handler.
     * If the player wasn't previously disconnected the socket isn't substituted.
     * @deprecated
     * Can't reuse the same handler, is easier to create a new one and assign him the old information of the player
     * @param socket the new socket created for the new connection with this player
     * @return true if the socket has been correctly substituted,
     *         false if the player is still connected before the call of this method
     */
    @Deprecated
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
            //initialize the player and do the configuration, exit from here when is assigned a MatchController
            init();

            CtoSMessage inMsg;
            System.out.println("------------- Player " + player + " enters the main cycle");
            while (inMatch.get()) {
                inMsg = read();
                inMsg.computeMessage(this);
            }

            //close reader, writer and socket connection, this is the correct total disconnection of the player
            terminateConnection(true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        finally {
            if(player != null)
                player.disconnect(); //todo: fix disconnection
            terminateConnection(false);
        }
    }

    /**
     * Initialization of the player, set writer and reader, talk with the client and set nickname and the correct match
     * @throws IOException if something goes wrong with the reading or writing with the player
     */
    private void init() throws IOException {
        CtoSMessage inMsg;
        initController = new InitController(this);

        while(!inMatch.get()) {
            inMsg = read();
            if (inMsg.getType().getCode() == 0)
                inMsg.computeMessage(this);
            else
                write(new RetryMessage(player == null ? null : player.getNickname(), "The match is not started yet, you cannot send messages like that"));
            //controls on existing nickname and previous players disconnection are done inside the computeMessage
        }

    }

    /**
     * Closes stream and socket, if removePlayer is true, remove also the player from the global list of players
     * this parameter is set false in abnormal conditions
     * @param removePlayer if true, remove also the player from the global list of players
     *                     is set false in abnormal conditions
     */
    private void terminateConnection(boolean removePlayer){
        try {
            in.close();
            out.close();
            socket.close();
        }catch (IOException ignored) { /*this exception is thrown when trying to close an already closed stream */}
        if(removePlayer) {
            System.out.println("Closed connection with " + player);
            removePlayer(this);
        }
    }

    /**
     * Reads a message CtoS from this client, if the message is not present, wait until a message is sent
     * @return the message read
     * @throws IOException if something goes wrong while waiting for the message
     */
    private CtoSMessage read() throws IOException {
        String readLine = null;
        CtoSMessage inMsg;
        while(true){
            try {
                readLine = in.readLine();
                System.out.println("Client wrote: "+readLine);
                inMsg = parserCtoS.fromJson(readLine, CtoSMessage.class);
                return inMsg;
            } catch (Exception e) {
                System.out.println("Arrived wrong message syntax from " + player + "\n--> message: " + readLine);
                this.write(new RetryMessage((player == null) ? null : player.getNickname(), "Wrong Json Syntax " + e.getMessage()));
            }
        }
    }

    /**
     * Writes something at this player, the message has to be written in json in the correct format
     * @param msg the message you want to send to this player
     * @return true if the message has been sent, false if something goes wrong in the output stream
     */
    public synchronized boolean write(StoCMessage msg){
        try {
            String outMsg = parserStoC.toJson(msg, StoCMessage.class);
            out.println(outMsg);
            return true;
        } catch (JsonSyntaxException e) {
            System.out.println("System shutdown due to internal error in parsing a StoC message");
            System.exit(1);
            return false;
        } catch (Exception exception) {
            System.out.println("error in write");
            return false;
        }
    }
}

