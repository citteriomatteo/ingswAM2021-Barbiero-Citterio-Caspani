package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.exceptions.DisconnectionException;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;
import it.polimi.ingsw.view.ClientController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.CLI.ClientCLI;

import java.io.*;
import java.net.Socket;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.cToSMessageConfig;
import static it.polimi.ingsw.jsonUtilities.GsonHandler.sToCMessageConfig;
import static it.polimi.ingsw.jsonUtilities.Preferences.ReadHostFromJSON;
import static it.polimi.ingsw.jsonUtilities.Preferences.ReadPortFromJSON;


public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final ClientController controller;

    //Build the parser for json input message
    private static final Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
    //Build the parser for json output message
    private static final Gson parserStoC = sToCMessageConfig(new GsonBuilder()).create();

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            System.out.println("System shutdown -> cannot connect with server");
            System.exit(1);
        }

        View cli = new ClientCLI();
        controller = new ClientController(cli);

    }

    private void startClient(){
        StoCMessage messageFromServer;
        boolean play = true;
        while (play){

            try {
                messageFromServer = readMessage();
            } catch (DisconnectionException e) {
                System.err.println(e.getMessage());
                return;
            }

            messageFromServer.compute(this);

            if(messageFromServer.getType().equals(StoCMessageType.GOODBYE))
                play = false;
            }
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
            String outMsg = parserCtoS.toJson(msg, CtoSMessage.class);
            //System.out.println("You write a " + msg.getType() + ":\n" + outMsg);
            if(!isAccepted(msg.getType())) {
                controller.printRetry("You're in "+controller.getCurrentState()+". Operation not available: retry. Write 'help' for message tips.");
                return false;
            }

            out.println(outMsg);
            return true;
        } catch (JsonSyntaxException e) {
            System.out.println("System shutdown -> Error in parsing a CtoS message");
            System.exit(1);
            return false;
        } catch (Exception exception) {
            System.out.println("Error in write");
            return false;
        }
    }


    /**
     * Closes streams and socket
     */
    private void terminateConnection(){
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

    public static void main(String[] args) {
        String hostName;
        int portNumber;
        if (args.length == 2) {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } else {
            hostName = ReadHostFromJSON();
            portNumber = ReadPortFromJSON();
        }
        Client client = new Client(hostName, portNumber); //CLI/gui choice param will be passed here
        new KeyboardReader(client).start();

        client.startClient();

        client.terminateConnection();
    }
}
