package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.server.ControlBase;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.cToSMessageConfig;
import static it.polimi.ingsw.gsonUtilities.GsonHandler.sToCMessageConfig;

public class Client
{

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final ExecutorService readQueue;
    private final Scanner keyboard;
    //Build the parser for json input message
    private static final Gson parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
    //Build the parser for json output message
    private static final Gson parserStoC = sToCMessageConfig(new GsonBuilder()).setPrettyPrinting().create();

    public Client(String address, int port) throws IOException {

        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        keyboard = new Scanner(System.in);
        readQueue = Executors.newSingleThreadExecutor();

    }
    //%%%%%%%%%%%% LITTLE SEQUENCE OF DIALOGUE %%%%%%%%%%%%%
    public void readPlainTextMessages() {
            while(true)
            {
                try {
                    String res = in.readLine();
                    System.out.println(res);
                    writeMessage(1);
                    res = in.readLine();
                    System.out.println(res);
                    writeMessage(2);
                } catch (IOException e) { e.printStackTrace(); }
            }
    }

    public void readMessages() {
        while(true)
        {
            try {
                String res = in.readLine();
                StoCMessage msg = parserStoC.fromJson(res, StoCMessage.class);
                //writeMessage(1); //TODO: this message will compute itself
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    //Real function that will be called, it will send the message to the server after parsing it.
    public synchronized boolean writeMessage(CtoSMessage msg){
        try {
            String outMsg = parserCtoS.toJson(msg, CtoSMessage.class);
            System.out.println("Hai scritto un "+msg.getType()+":\n"+outMsg);
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

    // %%%%%%%%%%%%%%%% stub controller of login phase (to try the communication) %%%%%%%%%%%%%%%%%%%
    public synchronized boolean writeMessage(int opz){
        try {
            CtoSMessage msg;
            String line=keyboard.nextLine();
            switch(opz)
            {
                case 1:
                    msg = new LoginMessage(line);
                    break;
                case 2:
                    boolean value;
                    if(line.equals("y"))
                        value=true;
                    else
                        value=false;
                    msg = new BinarySelectionMessage("" ,value, "");
                    break;
                default:
                    msg=null;
            }
            String outMsg = parserCtoS.toJson(msg, CtoSMessage.class);
            System.out.println("Hai scritto un "+msg.getType()+"\n:"+outMsg);
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





    public static void main(String args[]) {
        try {
            Client c = new Client("127.0.0.1", 1337);
            c.readPlainTextMessages();
        } catch (IOException e) {
            System.out.println("Not able to connect: server is down, retry later.");
            System.exit(1);
        }

    }
}
