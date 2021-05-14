package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class KeyboardReader extends Thread{
    private final BufferedReader keyboard;
    private final Client client;
    private static final Map<String, String> helpMap;
    static{
        helpMap = Map.ofEntries(
                entry("login", "[Nickname]"),
                entry("selection", "[y/n]"),
                entry("numPlayers","[numPlayers]"),
                entry("leadersChoice","[LeaderID]"),
                entry("startingResource","[ResourceType,Quantity]"),
                entry("switchShelf","[firstShelf,secondShelf]"),
                entry("leaderActivation","[LeaderID]"),
                entry("leaderDiscarding","[LeaderID]"),
                entry("marketDraw","[row?,number]"),
                entry("whiteMarblesConversion","[ResourceType,Quantity]"),
                entry("warehouseInsertion","[SingleResourceType,Shelf]"),
                entry("devCardDraw","[RowNumber,ColumnNumber]"),
                entry("payments","[-warehouse Shelf,ResourceType,Quantity -strongbox ResourceType,Quantity]"),
                entry("devCardPlacement","[Column]"),
                entry("production","[-cardsid cardID -ucosts ResourceType,Quantity -uearnings ResourceType,Quantity]"),
                entry("endTurn","[end_turn]"),
                entry("rematch","[y/n]")
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


    public void printHelpMap(Map<String, String> map){

        List<String> helpKeys = new ArrayList<>(map.keySet());
        List<String> helpValues = new ArrayList<>(map.values());

        for(int i = 0; i < helpKeys.size(); i++)
            System.out.println(i + ".  "+helpKeys.get(i) + " - " + helpValues.get(i));
    }
}
