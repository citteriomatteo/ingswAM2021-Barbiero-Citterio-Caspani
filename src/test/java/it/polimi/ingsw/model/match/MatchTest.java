package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.SlotEffect;
import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.FaithPathTest;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTest extends FaithPathTest {

    @Test
    public void JsonTest() {

        //Set the path where to find the file json
        String filePath = "src/test/resources/Example.json";

        //Build the parser for json file
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {
            LeaderCard slotLeader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                    new CardType(CardColor.GREEN, 1, 2))), 5,
                    new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

            LeaderCard slotLeader2 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                    new CardType(CardColor.BLUE, 1, 2))), 7,
                    new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

            DevelopmentCard devCard = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 1),
            devCard2 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 1);

            ArrayList<Cell> path = generatePath();

            Production prod = new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                    new ArrayList<>(List.of(new FaithPoint(1))));

            MatchConfiguration config = new MatchConfiguration(Arrays.asList(devCard,devCard2),
                    Arrays.asList(slotLeader,slotLeader2), path, prod);



            g.toJson(config, writer);

        } catch (IOException | NegativeQuantityException | InvalidQuantityException | InvalidAddFaithException e) {
            e.printStackTrace();
        }

        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
/*        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            LeaderCard extractedJson = g.fromJson(reader, LeaderCard.class);

//            System.out.println(extractedJson);
            assertEquals(extractedJson, slotLeader);

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
