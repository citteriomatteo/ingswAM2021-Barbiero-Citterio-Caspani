package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


import static java.util.Map.entry;

public class KeyboardReader extends Thread{
    private final BufferedReader keyboard;
    private final Client client;
    private static final Map<String, String> helpMap;
    static{
        helpMap = Map.ofEntries(
                entry("login", "[Nickname]")
        );
    }

    public KeyboardReader(Client client) {
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        this.client = client;
    }

    /**
     * Parse a message CtoS from this client
     * @param input the line read
     * @return the message read
     */
    private CtoSMessage parseInMessage(String input){
        String[] words = input.split("\\s+");

        return new LoginMessage("bau");
    }

    @Override
    public void run() {
        CtoSMessage messageToSend;
        String userInput;
        boolean play = true;
        while(play){
            try {
                userInput = keyboard.readLine();
                if(userInput.equals("exit"))
                    play = false;
                messageToSend = parseInMessage(userInput);
                client.writeMessage(messageToSend);

            } catch (IOException e) {
                System.out.println("Error while reading");
                e.printStackTrace();
            }
        }
    }
}
