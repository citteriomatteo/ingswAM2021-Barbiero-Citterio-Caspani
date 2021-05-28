package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.DisconnectionException;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.ClientController;
import it.polimi.ingsw.view.GUI.JavaFXGUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.CLI.ClientCLI;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.cToSMessageConfig;
import static it.polimi.ingsw.jsonUtilities.GsonHandler.sToCMessageConfig;
import static it.polimi.ingsw.jsonUtilities.Preferences.ReadHostFromJSON;
import static it.polimi.ingsw.jsonUtilities.Preferences.ReadPortFromJSON;
import static it.polimi.ingsw.network.server.TimeoutBufferedReader.getNewTimeoutBufferedReader;

//singleton
public class Client {
    private static final int PING_RATE = 30000; //30 seconds
    static private final Client instance = new Client();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private AtomicBoolean play;

    private ClientController controller = ClientController.getClientController();

    //Build the parser for json input message
    private static final Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
    //Build the parser for json output message
    private static final Gson parserStoC = sToCMessageConfig(new GsonBuilder()).create();


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

    private void setView(boolean cliChoice){
        if(cliChoice) {
            View view = new ClientCLI();
            new KeyboardReader(this).start();
            setController(view);
            startClient();
            return;
        }
        Application.launch(JavaFXGUI.class);
    }

    public void setController(View view) {
        this.controller = ClientController.getClientController();
        controller.setView(view);
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
            return parserStoC.fromJson(received, StoCMessage.class);
        } catch (Exception e) {
            System.out.println("Received Wrong json syntax from server" + e.getMessage());
            throw new DisconnectionException("The server is sending wrong messages, probably something has gone wrong, try to reconnect later");
        }

    }

    //Send the message to the server after parsing it.
    public synchronized boolean writeMessage(CtoSMessage msg) {
        try {
            if(msg.getType().equals(CtoSMessageType.BINARY_SELECTION) && controller.getCurrentState().equals(StateName.END_MATCH)) {
                boolean selection = ((BinarySelectionMessage) msg).getSelection();

                msg = new RematchMessage(msg.getNickname(), selection);

            }
            if(!isAccepted(msg.getType())) {
                controller.printRetry("You're in "+controller.getCurrentState()+". Operation not available: retry. Write 'help' for message tips.", controller.getCurrentState());
                return false;
            }

            String outMsg = parserCtoS.toJson(msg, CtoSMessage.class);
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

    public boolean isAccepted(CtoSMessageType type){ return controller.isAccepted(type); }

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
  //              System.out.println("It's ping time!");
                keepAlive();
            }
        };

        t.scheduleAtFixedRate(tt,6000,PING_RATE);
    }

    public static void main(String[] args) {
        String hostName;
        int portNumber;
        boolean cliChoice = false;
        if (args.length >= 2) {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
            if(args.length == 3)
                cliChoice = args[2].equals("--cli")||args[2].equals("-cli");
        } else {
            hostName = ReadHostFromJSON();
            portNumber = ReadPortFromJSON();
        }

        instance.setSocket(hostName, portNumber);

        instance.heartbeat();
        instance.setView(cliChoice);

        instance.terminateConnection();
    }
}
