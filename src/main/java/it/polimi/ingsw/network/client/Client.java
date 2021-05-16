package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessageType;

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

    }

    private void startClient(){
        StoCMessage messageFromServer;
        boolean play = true;
        while (play){
            messageFromServer = readMessage();
            if(messageFromServer.getType().equals(StoCMessageType.GOODBYE))
                play = false;
        }
    }

    private StoCMessage readMessage() {
        while (true) {
            try {
                String res = in.readLine();
                System.out.println("Server wrote: " + res);
                return parserStoC.fromJson(res, StoCMessage.class);
            } catch (Exception e) {
                System.out.println("Received Wrong json syntax " + e.getMessage());
            }
        }
    }

    //Send the message to the server after parsing it.
    public synchronized boolean writeMessage(CtoSMessage msg) {
        try {
            String outMsg = parserCtoS.toJson(msg, CtoSMessage.class);
            System.out.println("You write a " + msg.getType() + ":\n" + outMsg);
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
        Client client = new Client(hostName, portNumber);
        new KeyboardReader(client).start();

        client.startClient();

        client.terminateConnection();
    }
}
