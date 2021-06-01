package it.polimi.ingsw.network.client;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.DisconnectionException;
import it.polimi.ingsw.jsonUtilities.MyJsonParser;
import it.polimi.ingsw.jsonUtilities.Parser;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
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

//singleton
public class Client {
    private static final int PING_RATE = 30000; //30 seconds
    private static final Client instance = new Client();
    private static final Parser parser = MyJsonParser.getParser();
    private static final ClientController controller = ClientController.getClientController();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private AtomicBoolean play;

    private Client(){ }

    public static Client getClient(){
        return instance;
    }

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
        new Thread(()-> Application.launch(JavaFXGUI.class)).start();
    }


    public void startClient(){
        StoCMessage messageFromServer;
        while (play.get()){

            try {
                messageFromServer = readMessage();
            } catch (DisconnectionException e) {
                System.err.println(e.getMessage());
                return;
            }

            messageFromServer.compute(this);

            if(messageFromServer.getType().equals(StoCMessageType.GOODBYE))
                play.set(false);
            }
    }

    public void exit(){
        System.out.println("...Exiting from game");
        play.set(false);
        writeMessage(new DisconnectionMessage(getNickname()));
    }

    private StoCMessage readMessage() throws DisconnectionException{
        String received;
        try {
            received = in.readLine();
        } catch (IOException e) {
            throw new DisconnectionException("Cannot reach the server -> try to relaunch the program and connect later");
        }

        if(received == null)
                throw new DisconnectionException("Cannot reach the server -> try to relaunch the program and connect later");

        try{
            return parser.parseInStoCMessage(received);
        } catch (Exception e) {
            System.out.println("Received Wrong json syntax from server" + e.getMessage());
            throw new DisconnectionException("The server is sending wrong messages, probably something has gone wrong, try to reconnect later");
        }

    }

    //Send the message to the server after parsing it.
    public synchronized boolean writeMessage(CtoSMessage msg) {
        try {
            String outMsg = parser.parseFromCtoSMessage(msg);
            //System.out.println("You write a " + msg.getType() + ":\n" + outMsg);
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
     * Closes streams and socket
     */
    public void terminateConnection(){
        try {
            System.out.println("Connection closed");
            in.close();
            out.close();
            socket.close();
        }catch (IOException ignored) { /*this exception is thrown when trying to close an already closed stream */}
    }

    public ClientController getController() {
        return controller;
    }

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
