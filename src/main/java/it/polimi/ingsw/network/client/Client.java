package it.polimi.ingsw.network.client;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.gameLogic.exceptions.DisconnectionException;
import it.polimi.ingsw.jsonUtilities.MyJsonParser;
import it.polimi.ingsw.jsonUtilities.Parser;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.CLI.KeyboardReader;
import it.polimi.ingsw.view.ClientController;
import it.polimi.ingsw.view.GUI.ClientGUI;
import it.polimi.ingsw.view.GUI.JavaFXGUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.CLI.ClientCLI;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.network.server.TimeoutBufferedReader.getNewTimeoutBufferedReader;

/**
 * This singleton class implements the client operation: connecting, reading, writing, disconnecting, ...
 */
public class Client {
    private static final int PING_RATE = 30000; //30 seconds
    private static final Client instance = new Client();
    private static final Parser parser = MyJsonParser.getParser();
    private static final ClientController controller = ClientController.getClientController();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private AtomicBoolean play;

    /**
     * Private constructor of the Client, since it is a singleton no one should create an instance of this class.
     * This constructor doesn't set any parameter
     */
    private Client(){ }

    /**
     * Gets the instance of the Client singleton
     * @return the instance of the Client singleton
     */
    public static Client getClient(){
        return instance;
    }

    /**
     * This method sets the connection parameters and starts the connection.
     * It also initializes all the necessary components for reading/writing via input/output stream services.
     * @param address the ip address of the server
     * @param port the port
     */
    public void setSocket(String address, int port) {
        try {
            socket = new Socket(address, port);
            in = getNewTimeoutBufferedReader(socket);
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            System.out.println("System shutdown -> cannot connect with server");
            System.exit(1);
        }
        play = new AtomicBoolean(true);

    }

    /**
     * Creates the correct view, sets it inside the controller and creates a separate thread to handle player inputs
     * In case of CLI the thread is daemon, in case of GUI not
     * @param cliChoice if true set up a CLI, if false set up a GUI
     */
    public void setView(boolean cliChoice){
        if(cliChoice) {
            View view = new ClientCLI();
            new KeyboardReader(this).start();
            controller.setView(view);
            return;
        }

        View view = new ClientGUI();
        controller.setView(view);
        //new Thread(()-> Application.launch(JavaFXGUI.class)).start();
        Application.launch(JavaFXGUI.class);
    }

    /**
     * This method starts the client, beginning the reading cycle and computing each time a message is read and parsed.
     */
    public void startClient(){
        StoCMessage messageFromServer;
        while (play.get()){

            try {
                messageFromServer = readMessage();
            } catch (DisconnectionException e) {
                instance.getController().printGoodbyeMessage(e.getMessage());

                System.err.println(e.getMessage());
                return;
            }


            messageFromServer.compute(this);

            if(messageFromServer.getType().equals(StoCMessageType.GOODBYE))
                play.set(false);
            }

        terminateConnection();
    }

    /**
     * This method disconnects the player from the server.
     */
    public void exit(){
        System.out.println("...Exiting from game");
        play.set(false);
        writeMessage(new DisconnectionMessage(getNickname()));
    }

    /**
     * This method reads a message and parse it to an object message for computing.
     * @return the read message
     * @throws DisconnectionException in case the client cannot reach the server -> goodbyeMessage in this case.
     */
    private StoCMessage readMessage() throws DisconnectionException{
        String received;
        try {
            received = in.readLine();
        } catch (IOException e) {
            String msg = "Cannot reach the server -> try to relaunch the program and connect later";
            throw new DisconnectionException(msg);
        }

        if(received == null) {
            String msg = "Cannot reach the server -> try to relaunch the program and connect later";
            throw new DisconnectionException(msg);
        }

        try{
            return parser.parseInStoCMessage(received);
        } catch (Exception e) {
            System.out.println("Received Wrong json syntax from server" + e.getMessage());
            String msg = "The server is sending wrong messages, probably something has gone wrong, try to reconnect later";
            throw new DisconnectionException(msg);
        }

    }

    /**
     * This method sends the message to the server after parsing it.
     * @param msg the message still to parse
     * @return true if the send goes well
     */
    public synchronized boolean writeMessage(CtoSMessage msg) {
        try {
            String outMsg = parser.parseFromCtoSMessage(msg);
            out.println(outMsg);
            return true;
        } catch (JsonSyntaxException e) {
            System.out.println("System shutdown -> Error in parsing a CtoS message");
            System.exit(1);
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error in write");
            return false;
        }
    }

    /**
     * Keeps alive the connection with the server sending a ping
     */
    public synchronized void keepAlive(){
        out.println("ping");
    }


    /**
     * Closes streams and socket.
     */
    public void terminateConnection(){
        try {
            System.out.println("Connection closed");
            in.close();
            out.close();
            socket.close();
        }catch (IOException ignored) { /*this exception is thrown when trying to close an already closed stream */}
    }

    /**
     * Returns the clientController singleton, you can also get it calling {@link ClientController#getClientController()}
     * @return the clientController instance
     */
    public ClientController getController() {
        return controller;
    }

    /**
     * Returns the nickname associated to this client if there is already a nickname, otherwise returns null
     * @return the nickname associated to this client or null if this client hasn't a nickname yet
     */
    public String getNickname(){
        return controller == null ? null : controller.getNickname();
    }

    /**
     * Creates a thread that periodically calls {@link Client#keepAlive()} to keep alive the connection with the server
     */
    public void heartbeat(){
        Timer t = new Timer(true);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                keepAlive();
            }
        };

        t.scheduleAtFixedRate(tt,6000,PING_RATE);
    }
}
