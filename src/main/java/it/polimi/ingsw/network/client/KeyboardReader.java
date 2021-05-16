package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.Resource;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.sToCMessageConfig;
import static java.util.Map.entry;

public class KeyboardReader extends Thread{
    private final BufferedReader keyboard;
    private final Client client;
    private String nickname;
    private static final Map<String, String> helpMap;
    static{
        helpMap = Map.ofEntries(
                entry("login", "Nickname"),
                entry("selection", "y/n"),
                entry("numPlayers","numPlayers"),
                entry("leadersChoice","LeadersID"),
                entry("startingResource","ResourceType,Quantity"),
                entry("switchShelf","firstShelf,secondShelf"),
                entry("leaderActivation","LeaderID"),
                entry("leaderDiscarding","LeaderID"),
                entry("marketDraw","row,number"),
                entry("whiteMarblesConversion","ResourceType,Quantity"),
                entry("warehouseInsertion","SingleResourceType,Shelf"),
                entry("devCardDraw","RowNumber,ColumnNumber"),
                entry("payments","strongbox ResourceType,Quantity warehouse Shelf,ResourceType,Quantity"),
                entry("devCardPlacement","Column"),
                entry("production","cardsId cardID1,cardID2 uCosts ResourceType,Quantity uEarnings ResourceType,Quantity"),
                entry("endTurn","end_turn"),
                entry("rematch","y/n")
        );
    }

    public KeyboardReader(Client client) {
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        this.client = client;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Parse a message CtoS from this client
     * @param input the line read
     * @return the message read
     */
    private CtoSMessage parseInMessage(String input){
        List<String> words = new ArrayList(Arrays.asList(input.split("\\s+")));
        String command = words.get(0);
        List<String> params = words.subList(1, words.size());
        if(params.size() == 0){
            System.out.println("please insert a valid command");
            return null;
        }

        switch(command){
            case "login":
                return login(params);

            case "selection":
                return selection(params);

            case "numPlayers":
                return numPlayers(params);

            case "leadersChoice":
                return leadersChoice(params);

            case "startingResource":
                return startingResource(params);

            case "switchShelf":
                return switchShelf(params);

            case "leaderActivation":
                return leaderActivation(params);

            case "leaderDiscarding":
                return leaderDiscarding(params);

            case "marketDraw":
                return marketDraw(params);

            case "whiteMarblesConversion":
                return whiteMarblesConversion(params);

            case "warehouseInsertion":
                return warehouseInsertion(params);

            case "devCardDraw":
                return devCardDraw(params);

            case "payments":
                return payments(params);

            case "devCardPlacement":
                return devCardPlacement(params);

            case "production":
                return production(params);

            case "endTurn":
                return endTurn();

            case "rematch":
                return rematch(params);

            default:
                System.out.println("Wrong command, write \"help\" to print the possible commands");

        }
        return null;
    }

    private CtoSMessage login(List<String> params){
        if(params == null || params.size() > 1) {
            System.out.println("The nickname can't contains spaces. PLease insert a new nickname");
            return null;
        }
        nickname = params.get(0);
        return new LoginMessage(params.get(0));
    }

    private CtoSMessage selection(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("please insert only y for yes or n for no");
            return null;
        }
        return new BinarySelectionMessage(nickname, params.get(0).equals("y"));
    }

    private CtoSMessage numPlayers(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("please insert only an integer");
            return null;
        }
        int num;
        try{
            num = Integer.parseInt(params.get(0));
        }catch (NumberFormatException e){
            System.out.println("please insert only an integer");
            return null;
        }
        return new NumPlayersMessage(nickname, num);
    }

    private CtoSMessage leadersChoice(List<String> params){
        if(params == null){
            System.out.println("please insert a valid list of player");
            return null;
        }

        return new LeadersChoiceMessage(nickname, new ArrayList<>(List.of(params.get(0).split(","))));
    }

    private CtoSMessage startingResource(List<String> params){
        if(params == null){
            System.out.println("please insert a valid list of resources");
            return null;
        }
        List<PhysicalResource> resources = new ArrayList<>();
        for (String param : params) {
            PhysicalResource resource = parseInPhysicalResource(param);
            resources.add(resource);
        }

        return new StartingResourcesMessage(nickname, resources);
    }

    private CtoSMessage switchShelf(List<String> params){
        if(params == null || params.size() != 1){
            System.out.println("please insert a valid choice for shelves");
            return null;
        }
        List<String> elements = List.of(params.get(0).split(","));
        int shelf1;
        int shelf2;
        try{
            shelf1 = Integer.parseInt(elements.get(0));
            shelf2 = Integer.parseInt(elements.get(1));
        }catch (NumberFormatException e){
            System.out.println("please insert an integer");
            return null;
        }

        return new SwitchShelfMessage(nickname, shelf1, shelf2);
    }

    private CtoSMessage leaderActivation(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("you have to chose only one leader");
            return null;
        }

        return new LeaderActivationMessage(nickname, params.get(0));
    }

    private CtoSMessage leaderDiscarding(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("you have to chose only one leader");
            return null;
        }
        return new LeaderDiscardingMessage(nickname, params.get(0));
    }

    private CtoSMessage marketDraw(List<String> params){
        if(params == null || params.size() != 1){
            System.out.println("please select a valid choice of row/column");
            return null;
        }
        List<String> elements = List.of(params.get(0).split(","));
        if(elements.size() != 2)
            return null;

        return new MarketDrawMessage(nickname, elements.get(0).equals("true"), Integer.parseInt(elements.get(1)));

    }

    private CtoSMessage whiteMarblesConversion(List<String> params){
        if(params == null){
            System.out.println("please select some resources");
            return null;
        }
        List<PhysicalResource> resources = new ArrayList<>();
        for (String param : params) {
            PhysicalResource resource = parseInPhysicalResource(param);
            resources.add(resource);
        }

        return new WhiteMarblesConversionMessage(nickname, resources);
    }

    private CtoSMessage warehouseInsertion(List<String> params){
        if(params == null){
            System.out.println("please select some resources");
            return null;
        }
        List<PhysicalResource> resources = new ArrayList<>();
        for (String param : params) {
            PhysicalResource resource = parseInPhysicalResource(param);
            resources.add(resource);
        }

        return new WarehouseInsertionMessage(nickname, resources);
    }

    private CtoSMessage devCardDraw(List<String> params){
        if(params == null || params.size() != 2){
            System.out.println("please select a row and a column");
        }
        List<String> elements = List.of(params.get(0).split(","));
        if(elements.size() != 2)
            return null;

        return new DevCardDrawMessage(nickname, Integer.parseInt(elements.get(0)), Integer.parseInt(elements.get(1)));
    }

    private CtoSMessage payments(List<String> params){
        if(params == null || params.size() < 2){
            System.out.println("please insert a valid list of resources");
        }
        PhysicalResource voidResource = null;
        try {
            voidResource = new PhysicalResource(ResType.UNKNOWN, 0);
        } catch (NegativeQuantityException e) {
            System.exit(1);
        }
        List<PhysicalResource> strongboxCosts = new ArrayList<>();
        Map<Integer,PhysicalResource> warehouseCosts = new HashMap<>();

        int i=0;
        String element;
        if(params.get(i).equals("strongbox")){
            i++;
            element = params.get(i);
            while (!element.equals("warehouse")) {
                PhysicalResource resource = parseInPhysicalResource(element);
                strongboxCosts.add(resource);
                i++;
                if (i == params.size())
                    break;
                element = params.get(i);
            }
        }
        else
            strongboxCosts.add(voidResource);

        if(i == params.size()) {
            warehouseCosts.put(0, voidResource);
            return new PaymentsMessage(nickname, strongboxCosts, warehouseCosts);
        }

        if(params.get(i).equals("warehouse")){
            for (int j=i+1; j<params.size(); j++){
                element = params.get(j);
                if(!addWarehouseCosts(element, warehouseCosts))
                    warehouseCosts = null;
            }
        }
        else warehouseCosts.put(0, voidResource);


        return new PaymentsMessage(nickname, strongboxCosts, warehouseCosts);
    }

    private CtoSMessage production(List<String> params){
        if(params == null || params.size() < 2){
            System.out.println("please insert a valid production message");
            return null;
        }

        if(!params.get(0).equals("cardsId")){
            System.out.println("please insert the cards you want to produce");
            return null;
        }
        List<PhysicalResource> uCosts = new ArrayList<>();
        List<Resource> uEarnings = new ArrayList<>();
        List<String> IDs = new ArrayList<>();

        PhysicalResource voidResource = null;
        try {
            voidResource = new PhysicalResource(ResType.UNKNOWN, 0);
        } catch (NegativeQuantityException e) {
            System.exit(1);
        }


        PhysicalResource cost;
        Resource earning;

        int i=1;
        String element;
        element = params.get(i);
        while (!element.equals("uCosts") && !element.equals("uEarnings")){
            IDs.add(element);
            i++;
            if (i == params.size())
                break;
            element = params.get(i);
        }

        if(i == params.size()) {
            uCosts.add(voidResource);
            uEarnings.add(voidResource);

            return new ProductionMessage(nickname, IDs, new Production(uCosts, uEarnings));
        }

        if(params.get(i).equals("uCosts")) {
            i++;
            element = params.get(i);
            while (!element.equals("uEarnings")) {
                cost = parseInPhysicalResource(element);
                uCosts.add(cost);
                i++;
                if (i == params.size())
                    break;
                element = params.get(i);
            }

            if (i == params.size()) {
                uEarnings.add(voidResource);
                return new ProductionMessage(nickname, IDs, new Production(uCosts, uEarnings));
            }
        }
        else
            uCosts.add(voidResource);

        if(params.get(i).equals("uEarnings")) {
            i++;
            do {
                element = params.get(i);
                earning = parseInPhysicalResource(element);
                uEarnings.add(earning);
                i++;
            } while (i < params.size());
        }
        else
            uEarnings.add(voidResource);

        return new ProductionMessage(nickname, IDs, new Production(uCosts, uEarnings));

    }

    private CtoSMessage devCardPlacement(List<String> params){
        if(params == null || params.size() != 1){
            System.out.println("please select a valid column");
        }
        int num ;
        try {
            num = Integer.parseInt(params.get(0));
        }catch (NumberFormatException e){ return null;}
        return new DevCardPlacementMessage(nickname, num);
    }

    private CtoSMessage endTurn(){
        return new EndTurnMessage(nickname);
    }

    private CtoSMessage rematch(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("please insert only y for yes or n for no");
        }
        return new RematchMessage(nickname, params.get(0).equals("y"));
    }

    private PhysicalResource parseInPhysicalResource(String param){
        List<String> elements = List.of(param.split(","));
        if(elements.size() != 2)
            return null;

        String stringType = elements.get(0);
        int quantity;
        try {
            quantity = Integer.parseInt(elements.get(1));
        }catch (NumberFormatException e){
            System.out.println("please insert an integer");
            return null;
        }

        ResType type;
        switch (stringType){
            case "coin":
                type = ResType.COIN;
                break;

            case "shield":
                type = ResType.SHIELD;
                break;

            case "stone":
                type = ResType.STONE;
                break;

            case "servant":
                type = ResType.SERVANT;
                break;

            default:
                type = ResType.UNKNOWN;
                break;
        }

        if(type.ordinal() == ResType.UNKNOWN.ordinal()) {
            System.out.println("please insert a valid type");
            return null;
        }

        try {
            return new PhysicalResource(type, quantity);
        } catch (NegativeQuantityException e) {
            System.out.println("please insert an integer");
            return null;
        }
    }

    private boolean addWarehouseCosts(String param, Map<Integer, PhysicalResource> warehouseCosts){
        int shelf;
        PhysicalResource resource;
        List<String> elements = new ArrayList<>(Arrays.asList(param.split(",")));
        if(elements.size() != 3)
            return false;
        try {
            shelf = Integer.parseInt(elements.get(0));
        }catch (NumberFormatException e) {
            System.out.println("please insert an integer");
            return false;
        }

        resource = parseInPhysicalResource(elements.get(1).concat(",").concat(elements.get(2)));

        warehouseCosts.put(shelf, resource);

        return true;
    }

    @Override
    public void run() {
        CtoSMessage messageToSend;
        String userInput;
        boolean play = true;
        while(play){
            try {
                userInput = keyboard.readLine();
                if(userInput == null) {
                    System.out.println("please insert a valid command");
                    continue;
                }
                if (!userInput.matches(".*\\w.*")) {
                    System.out.println("please insert a valid command");
                    continue;
                }
                if(userInput.equals("help")) {
                    printHelpMap();
                    continue;
                }
                if(userInput.equals("exit"))
                    play = false;
                messageToSend = parseInMessage(userInput);
                if(messageToSend == null)
                    continue;
                client.writeMessage(messageToSend);

            } catch (IOException e) {
                System.out.println("Error while reading");
                e.printStackTrace();
            }
        }
    }

    public void printHelpMap(){

        List<String> helpKeys = new ArrayList<>(helpMap.keySet());
        List<String> helpValues = new ArrayList<>(helpMap.values());

        for(int i = 0; i < helpKeys.size(); i++)
            System.out.println(i + ".  "+helpKeys.get(i) + " : " + helpValues.get(i));
    }

}
