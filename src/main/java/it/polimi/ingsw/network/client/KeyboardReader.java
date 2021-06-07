package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.network.message.ctosmessage.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static it.polimi.ingsw.controller.StateName.*;
import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static java.util.Map.entry;

public class KeyboardReader extends Thread{
    private final BufferedReader keyboard;
    private final Client client;
    private String nickname;
    private static final Map<StateName,String> helpMap;
    static {
        helpMap = Map.ofEntries(
                entry(LOGIN, "\u2022 login [nickname]"),
                entry(RECONNECTION, "\u2022 [y/n]"),
                entry(NEW_PLAYER, "\u2022 [single/multi]"),
                entry(NUMBER_OF_PLAYERS, "\u2022 numPlayers|(np) [numPlayers]"),
                entry(SP_CONFIGURATION_CHOOSE, "\u2022 [y/n]"),
                entry(MP_CONFIGURATION_CHOOSE, "\u2022 [y/n]"),
                entry(WAITING, "Waiting...."),
                entry(WAITING_FOR_PLAYERS, "Please wait.... other players are connecting to your match"),
                entry(START_GAME, "\u2022 leadersChoice|(lc) [LeadersID]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(WAITING_LEADERS, "\u2022 leadersChoice|(lc) [LeadersID]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(WAITING_RESOURCES, "\u2022 startingResource|(sr) [ResourceType,Shelf]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(WAITING_FOR_TURN, "It's not your turn.... please wait"),
                entry(STARTING_TURN, "\u2022 leaderActivation|(la) [LeaderID]\n" +
                        "\u2022 leaderDiscarding|(ld) [LeaderId]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 marketDraw|(md) [r/c|(row/column),number]\n" +
                        "\u2022 devCardDraw|(dd) [RowNumber,ColumnNumber]\n" +
                        "\u2022 production|(prod) [-cardsId|(-cid) cardIDs] [-uCosts|(-uc) ResourceType,Quantity] [-uEarnings|(-ue) ResourceType,Quantity]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(MARKET_ACTION, "\u2022 whiteMarblesConversion|(wmc) [ResourceType,Quantity]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(RESOURCES_PLACEMENT, "\u2022 warehouseInsertion|(wi) [SingleResourceType,Shelf]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(BUY_DEV_ACTION, "\u2022 payments|(pay) [-strongbox|(-sb) ResourceType,Quantity] [-warehouse|(-wh) Shelf,ResourceType,Quantity]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(PLACE_DEV_CARD, "\u2022 devCardPlacement|(dp) [Column]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(PRODUCTION_ACTION, "\u2022 payments|(pay) [-strongbox|(-sb) ResourceType,Quantity] [-warehouse|(-wh) Shelf,ResourceType,Quantity]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(END_TURN, "\u2022 leaderActivation|(la) [LeaderID]\n" +
                        "\u2022 leaderDiscarding|(ld) [LeaderId]\n" +
                        "\u2022 switchShelf|(ss) [firstShelf,secondShelf]\n" +
                        "\u2022 endTurn|(et)\n" +
                        "\u2022 cardInfo|(ci) [cardIDs]"),
                entry(END_MATCH, "\u2022 rematch|(rm) [y/n]"),
                entry(REMATCH_OFFER, "\u2022 rematch|(rm) [y/n]")
        );
    }

    public KeyboardReader(Client client) {
        setDaemon(true);
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
        List<String> words = new ArrayList(Arrays.asList(input.toLowerCase().split("\\s+")));
        String command = words.get(0);
        List<String> params = words.subList(1, words.size());
        if(params.size() == 0){
            if(command.equals("endturn") || command.equals("et"))
                endTurn();
            else if(command.equals("warehouseinsertion") || command.equals("wi")){
                return new WarehouseInsertionMessage(nickname, new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,0))));
            }
            else{
                CtoSMessage message = selection(command);
                if(message == null) {
                    System.out.println("please insert a valid command");
                    return null;
                }
                else return message;
            }
        }

        switch(command){
            case "login":
                return login(params);

            case "selection":
                return selection(params.get(0));

            case "np":
            case "numplayers":
                return numPlayers(params);

            case "lc":
            case "leaderschoice":
                return leadersChoice(params);

            case "sr":
            case "startingresource":
                return startingResource(params);

            case "ss":
            case "switchshelf":
                return switchShelf(params);

            case "la":
            case "leaderactivation":
                return leaderActivation(params);

            case "ld":
            case "leaderdiscarding":
                return leaderDiscarding(params);

            case "md":
            case "marketdraw":
                return marketDraw(params);

            case "wmc":
            case "whitemarblesconversion":
                return whiteMarblesConversion(params);

            case "wi":
            case "warehouseinsertion":
                return warehouseInsertion(params);

            case "dd":
            case "devcarddraw":
                return devCardDraw(params);

            case "pay":
            case "payments":
                return payments(params);

            case "dp":
            case "devcardplacement":
                return devCardPlacement(params);

            case "prod":
            case "production":
                return production(params);

            case "et":
            case "endturn":
                return endTurn();

            case "rm":
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
        getClientController().setNickname(nickname);
        return new LoginMessage(params.get(0));
    }

    private CtoSMessage selection(String input){

        boolean choice;
        switch (input) {
            case "y":
            case "yes":
            case "single":
                choice = true;
                break;
            case "n":
            case "no":
            case "multi":
                choice = false;
                break;
            default:
                System.out.println("invalid command");
                return null;
        }

        return new BinarySelectionMessage(nickname, choice);
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

        return new LeadersChoiceMessage(nickname, new ArrayList<>(List.of(params.get(0).toUpperCase().split(","))));
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

        return new LeaderActivationMessage(nickname, params.get(0).toUpperCase());
    }

    private CtoSMessage leaderDiscarding(List<String> params){
        if(params == null || params.size() > 1){
            System.out.println("you have to chose only one leader");
            return null;
        }

        //deleting the discarded leader without waiting for a server's response.
        client.getController().getMatch().getLightPlayer(getClient().getNickname()).getHandLeaders().remove(params.get(0).toUpperCase());


        return new LeaderDiscardingMessage(nickname, params.get(0).toUpperCase());
    }

    private CtoSMessage marketDraw(List<String> params){
        if(params == null || params.size() != 1){
            System.out.println("please select a valid choice of row/column");
            return null;
        }
        List<String> elements = List.of(params.get(0).split(","));
        if(elements.size() != 2)
            return null;

        String input = elements.get(0);
        boolean choice;
        if(input.equals("row") || input.equals("r"))
            choice = true;
        else if(input.equals("column") || input.equals("c"))
            choice = false;
        else{
            System.out.println("please insert r/row or c/column");
            return null;
        }

        int num;
        try {
            num = Integer.parseInt(elements.get(1));
        }catch (NumberFormatException e){
            System.out.println("please insert an integer");
            return null;
        }

        return new MarketDrawMessage(nickname, choice, num);

    }

    private CtoSMessage whiteMarblesConversion(List<String> params){
        List<PhysicalResource> resources = parseInPhysicalResourcesList(params);
        if(resources == null)
            return null;

        return new WhiteMarblesConversionMessage(nickname, resources);
    }

    private List<PhysicalResource> parseInPhysicalResourcesList(List<String> params){
        if(params == null){
            System.out.println("please select some resources");
            return null;
        }
        List<PhysicalResource> resources = new ArrayList<>();
        for (String param : params) {
            PhysicalResource resource = parseInPhysicalResource(param);
            if (resource == null)
                return resources;
            resources.add(resource);
        }
        return resources;
    }

    private CtoSMessage warehouseInsertion(List<String> params){
        List<PhysicalResource> resources = parseInPhysicalResourcesList(params);
        if(resources == null)
            return null;

        return new WarehouseInsertionMessage(nickname, resources);
    }

    private CtoSMessage devCardDraw(List<String> params){
        if(params == null || params.size() != 2){
            System.out.println("please select a row and a column");
        }
        List<String> elements = List.of(params.get(0).split(","));
        if(elements.size() != 2)
            return null;
        int row;
        int column = 0;
        try {
            row = Integer.parseInt(elements.get(0));
            column = Integer.parseInt(elements.get(1));
        }catch (NumberFormatException e){
            System.out.println("please insert an integer");
            return null;
        }

        return new DevCardDrawMessage(nickname, row, column);
    }

    private CtoSMessage payments(List<String> params){
        int numParams = params.size();
        if( numParams < 2){
            System.out.println("please insert a valid list of resources");
            return null;
        }
        PhysicalResource voidResource = new PhysicalResource(ResType.UNKNOWN, 0);

        List<PhysicalResource> strongboxCosts = new ArrayList<>();
        Map<Integer,PhysicalResource> warehouseCosts = new HashMap<>();

        if(params.contains("-strongbox") || params.contains("-sb")) {
            List<PhysicalResource> temp = parseInPhysicalResourcesList(params
                    .subList(Math.max(params.indexOf("-strongbox"), params.indexOf("-sb")) + 1, numParams));
            if (!(temp == null || temp.size() == 0))
                strongboxCosts.addAll(temp);
            else
                strongboxCosts.add(voidResource);
        }
        else
            strongboxCosts.add(voidResource);

        String element;
        if(params.contains("-warehouse") || params.contains("-wh")) {
            int index = Math.max(params.indexOf("-warehouse"), params.indexOf("-wh")) + 1;
            for (int j=index; j<numParams; j++){
                element = params.get(j);
                if(element.startsWith("-"))
                    break;
                if(!addWarehouseCosts(element, warehouseCosts))
                    return null;
            }
        }
        else warehouseCosts.put(0, voidResource);

        return new PaymentsMessage(nickname, strongboxCosts, warehouseCosts);
    }

    private boolean isValidCardID(String element){
        return(((element.startsWith("l")||element.startsWith("d")) && element.substring(1).matches("\\d+")) || element.equals("basicprod") || element.equals("bp"));
    }

    private CtoSMessage production(List<String> params){
        int numParams = params.size();
        if(numParams < 2){
            System.out.println("please insert a valid production message");
            return null;
        }

        if(!(params.contains("-cardsid")|| params.contains("-cid"))){
            System.out.println("please insert the cards you want to produce");
            return null;
        }

        List<PhysicalResource> uCosts = new ArrayList<>();
        List<Resource> uEarnings = new ArrayList<>();
        List<String> IDs = new ArrayList<>();

        PhysicalResource voidResource = new PhysicalResource(ResType.UNKNOWN, 0);

        int index = (Math.max(params.indexOf("-cardsid"), params.indexOf("-cid"))+1);
        String element = params.get(index);
        for (int i=index; i < numParams && element != null && !element.startsWith("-");) {

            if(!isValidCardID(element)){
                System.out.println("Please insert only valid CardID type");
                return null;
            }
            if(element.equals("bp"))
                IDs.add("BASICPROD");
            else
                IDs.add(element.toUpperCase());

            try {
                element = params.get(++i);
            }catch (IndexOutOfBoundsException e){
                element = null;
            }
        }

        if(params.contains("-ucosts")||params.contains("-uc")) {
            List<PhysicalResource> temp = parseInPhysicalResourcesList(params
                    .subList(Math.max(params.indexOf("-ucosts"), params.indexOf("-uc")) + 1, numParams));
            if (!(temp == null || temp.size() == 0))
                uCosts.addAll(temp);
        }
        if(params.contains("-uearnings")||params.contains("-ue")) {
            List<PhysicalResource> temp = parseInPhysicalResourcesList(params
                    .subList(Math.max(params.indexOf("-uearnings"), params.indexOf("-ue")) + 1, numParams));
            if (!(temp == null || temp.size() == 0))
                uEarnings.addAll(temp);
        }

        if(uCosts.size()==0)
            uCosts.add(voidResource);

        if(uEarnings.size()==0)
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

        if(type == ResType.UNKNOWN) {
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
        if(elements.size() != 3) {
            System.out.println("Wrong writing of parameters");
            return false;
        }
        try {
            shelf = Integer.parseInt(elements.get(0));
        }catch (NumberFormatException e) {
            System.out.println("please insert an integer as shelf");
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
        while(true){
            try {
                userInput = keyboard.readLine();
                if(userInput == null) {
                    System.out.println("please insert a valid command");
                    continue;
                }
                userInput = userInput.toLowerCase();
                if (!userInput.matches(".*\\w.*")) {
                    System.out.println("please insert a valid command");
                    continue;
                }
                if(userInput.equals("help") || userInput.equals("h")) {
                    printHelpMap();
                    continue;
                }
                if(userInput.equals("discountmap") || userInput.equals("dm")) {
                    getClientController().printDiscountMap(nickname);
                    continue;
                }
                if(userInput.equals("whitemarbleconversions") || userInput.equals("wmc")) {
                    getClientController().printWhiteMarbleConversions(nickname);
                    continue;
                }
                if(userInput.indexOf("cardinfo") == 0 || userInput.indexOf("ci") == 0) {
                    getClientController().printCardInfo(userInput);
                    continue;
                }
                if(userInput.equals("exit"))
                    break;
                messageToSend = parseInMessage(userInput);
                if(messageToSend == null)
                    continue;
                messageToSend.send();

            } catch (IOException e) {
                System.out.println("Error while reading");
                e.printStackTrace();
            }
        }
        client.exit();
    }

    public void printHelpMap() {
        StateName currentState = getClientController().getCurrentState();

        for (StateName state : helpMap.keySet())
            if(state.equals(currentState)) {
                System.out.println("These are your possible moves:");
                System.out.println(helpMap.get(currentState));
            }
    }

}
