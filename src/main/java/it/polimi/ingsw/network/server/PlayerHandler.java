package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.InitController;
import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.DisconnectionException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.GoodbyeMessage;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.network.server.ServerUtilities.*;

/**
 * This class manages the direct talk with the player, every exchanged message between client
 * and server has to pass from here
 */
public class PlayerHandler implements Runnable, ControlBase {
//    private static final int WAIT_FOR_READING = 60000; //1 minute

    //Build the parser for json input message
    private static final Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
    //Build the parser for json output message
    private static final Gson parserStoC = sToCMessageConfig(new GsonBuilder()).create();

    private final Socket socket;
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
    public StateName getCurrentState(){
        if(inMatch.get())
            return matchController.getCurrentState(player.getNickname());
        return initController.getCurrentState();
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
    public String getNickname() {
        return player == null ? null : player.getNickname();
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


//%%%%%%%%%%%%%%%% MAIN FUNCTION %%%%%%%%%%%%%%%%%%%%

    public void run() {
        try {
            //initialize the player and do the configuration, exit from here when is assigned a MatchController
            initializationAndSetting();

            playTheGame();

            //if the game comes naturally to an end remove the player from the global list
            terminateConnection(true);

        } catch (IOException | DisconnectionException e) {
            disconnection();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println("Unknown exception for player " +getNickname()+". Inform the client if possible and close the connection");
            if(!socket.isClosed())
                write(new GoodbyeMessage(getNickname(), "I'm sorry the server is temporary offline, retry soon", true));
            disconnection();
        }
    }

    /**
     * Initialization of the player, set writer and reader, talk with the client and set nickname and the correct match
     * @throws IOException if something goes wrong with the reading or writing with the player
     * @throws DisconnectionException if the client disconnects
     */
    private void initializationAndSetting() throws IOException, DisconnectionException {
        CtoSMessage inMsg;
        initController = new InitController(this);

        while(!inMatch.get()) {
            inMsg = read();
            if (inMatch.get()) { //The player entered the next cycle but is actually in the match, so the message has to be computed before returning
                inMsg.computeMessage(this);
                return;
            }
            if (inMsg.getType().getCode() == 0)
                inMsg.computeMessage(this);
            else
                write(new RetryMessage(getNickname(), getCurrentState(),  "The match is not started yet, you cannot send messages like that"));
        }
    }


    private void playTheGame() throws IOException, DisconnectionException {
        CtoSMessage inMsg;
        while (inMatch.get()) {
            inMsg = read();
            inMsg.computeMessage(this);
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
            System.out.println("Closed connection with " + player + " and removed it from global list");
            serverCall().removePlayer(this);
        }
    }

    /**
     * Reads a message CtoS from this client, if the message is not present, wait until a message is sent,
     * if the player disconnects interrupt the reading and set the player disconnected then throw the DisconnectionException
     * @return the message read
     * @throws IOException if something goes wrong while waiting for the message
     * @throws DisconnectionException if the player disconnects
     */
    private CtoSMessage read() throws IOException, DisconnectionException {
        String readLine;
        CtoSMessage inMsg;
        while(true){
                readLine = in.readLine();
                System.out.println("Client wrote: "+readLine);
                if(readLine == null)
                    throw new DisconnectionException("player " + player + " disconnected");
                if(!readLine.equals(""))
                    try {
                        inMsg = parserCtoS.fromJson(readLine, CtoSMessage.class);
                        return inMsg;
                    } catch (Exception e) {
                        System.out.println("Arrived wrong message syntax from " + player + "\n--> message: " + readLine);
                        this.write(new RetryMessage(getNickname(), getCurrentState(), "Wrong Json Syntax " + e.getMessage()));
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
            exception.printStackTrace();
            System.out.println("error in write");
            return false;
        }
    }

    public synchronized void disconnection(){

        System.err.println("Probably something goes wrong or the player "+getNickname()+" closed the connection --> disconnection");
        if(inMatch.get()) {
            terminateConnection(false);
            if (!player.disconnect())
                System.err.println("Tried to disconnect a previously disconnected player");

            matchController.disconnection(player); //notify other players here
            return;
        }

        if(player!=null) {
            StateName currentState = initController.getCurrentState();
            if (currentState == StateName.NUMBER_OF_PLAYERS || currentState == StateName.MP_CONFIGURATION_CHOOSE)
                serverCall().rejectPriority(player);
            //since the player is not yet in game -> remove totally his nickname from the server
            terminateConnection(true);
        }
    }
}

