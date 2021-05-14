package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.model.essentials.Production;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @Test
    public void parsingTest() throws NegativeQuantityException {
        //Set the path where to find the file json
        String filePath = "src/test/resources/MessageExample.json";

        String nickname = "Ale";
        //Build the parser for json file
        Gson g = cToSMessageConfig(new GsonBuilder()).create();

        CtoSMessage switchShelfMessage = new SwitchShelfMessage(nickname, 1,2);
        CtoSMessage marketDrawMessage = new MarketDrawMessage(nickname, true,1);
        CtoSMessage loginMessage = new LoginMessage(nickname);
        CtoSMessage leadersChoiceMessage = new LeadersChoiceMessage(nickname, List.of("L1", "L2"));
        CtoSMessage startingResourcesMessage = new StartingResourcesMessage(nickname, List.of(new PhysicalResource(ResType.SHIELD, 1)));
        CtoSMessage leaderActivationMessage = new LeaderActivationMessage(nickname, "L1");

        List<CtoSMessage> messages = new ArrayList<>();

        messages.add(loginMessage);
        messages.add(new BinarySelectionMessage(nickname, true));
        messages.add(new NumPlayersMessage(nickname, 3));
        messages.add(leadersChoiceMessage);
        messages.add(startingResourcesMessage);

        messages.add(switchShelfMessage);
        messages.add(leaderActivationMessage);
        messages.add(new LeaderDiscardingMessage(nickname, "L1"));

        messages.add(marketDrawMessage);
        messages.add(new WhiteMarblesConversionMessage(nickname, List.of(new PhysicalResource(ResType.SHIELD, 1))));
        messages.add(new WarehouseInsertionMessage(nickname, List.of(new PhysicalResource(ResType.SHIELD, 1))));

        messages.add(new DevCardDrawMessage(nickname, 2,1));
            Map<Integer, PhysicalResource> whPayments = new HashMap<>();
            whPayments.put(1, new PhysicalResource(ResType.STONE,1));
            whPayments.put(2, new PhysicalResource(ResType.SHIELD,1));
        messages.add(new PaymentsMessage(nickname,
                List.of(new PhysicalResource(ResType.STONE,1)), whPayments));
        messages.add(new DevCardPlacementMessage(nickname, 1));

        messages.add(new ProductionMessage(nickname, List.of("L1", "L2"), new Production(List.of(new PhysicalResource(ResType.COIN, 1)), List.of(new PhysicalResource(ResType.STONE, 1)))));

        messages.add(new EndTurnMessage(nickname));

        messages.add(new RematchMessage(nickname, true));




        //extrapolate the type of the collection
        Type collectionType = new TypeToken<ArrayList<CtoSMessage>>(){}.getType();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {

            g.toJson(messages, collectionType, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            ArrayList<CtoSMessage> extractedJson = g.fromJson(reader, collectionType);

            System.out.println(extractedJson);
            assertEquals(extractedJson.get(0).getNickname(), switchShelfMessage.getNickname());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
