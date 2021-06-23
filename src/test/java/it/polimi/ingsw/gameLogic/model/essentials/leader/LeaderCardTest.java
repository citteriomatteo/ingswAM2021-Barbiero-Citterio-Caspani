package it.polimi.ingsw.gameLogic.model.essentials.leader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.gameLogic.model.essentials.CardColor;
import it.polimi.ingsw.gameLogic.model.essentials.CardType;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.MultiMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.PersonalBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static it.polimi.ingsw.jsonUtilities.GsonHandler.effectConfig;
import static it.polimi.ingsw.jsonUtilities.GsonHandler.requirableConfig;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardTest extends CommonThingsTest {
    private LeaderCard slotLeader;

    //Tests the constructor
    @Test
    @BeforeEach
    public void ConstructorTest() throws InvalidQuantityException {

        slotLeader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));
    }

    //Test the writing on json file for LeaderCard and instantiation of LeaderCard via json file
    @Test
    public void JsonTest() {

        //Set the path where to find the file json
        String filePath = "src/test/resources/SlotLeaderExample.json";

        //Build the parser for json file
        Gson g = requirableConfig(effectConfig(new GsonBuilder())).setPrettyPrinting().create();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {
            g.toJson(slotLeader, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            LeaderCard extractedJson = g.fromJson(reader, LeaderCard.class);

            assertEquals(extractedJson, slotLeader);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Test the activation of all kinds of leaders
    // It prepares the match with a player and then activate all the four leaders in his hand
    // verifying that have had the desired effect
    @RepeatedTest(5)
    public void leaderActivationTest() throws WrongSettingException, SingleMatchException {
        muteOutput();

        PhysicalResource effectResource;
        List<Player> players = new ArrayList<>(List.of(new Player("Giorgio"), new Player("Luca")));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        Match match1 = new MultiMatch(players);
        Player giorgio = match1.getCurrentPlayer();
        PersonalBoard personalBoard = giorgio.getPersonalBoard();
        List<LeaderCard> handLeaders = giorgio.getHandLeaders();
        LeaderCard leader;
        Effect effect;
        for (int i = 0; i < 4; i++) {

            leader = handLeaders.get(i);
            effect = leader.getEffect();
            System.out.println("--> Found a " + effect.getClass().getName() + " leader");
            System.out.println(effect);

            if (effect instanceof DiscountEffect) {
                effectResource = ((DiscountEffect) effect).getDiscount();
                leader.activate(personalBoard);
                PhysicalResource cheapResource = personalBoard.getDiscountMap().applyDiscount(effectResource);
                System.out.println("effect: " + effectResource + " cheap: " + cheapResource);
                assertEquals(0, cheapResource.getQuantity());

            } else if (effect instanceof SlotEffect) {
                int previousDimension = personalBoard.getWarehouse().getWarehouseDisposition().size();
                System.out.println(personalBoard.getWarehouse().getWarehouseDisposition());
                leader.activate(personalBoard);
                System.out.println(personalBoard.getWarehouse().getWarehouseDisposition());
                int evolvedDimension = personalBoard.getWarehouse().getWarehouseDisposition().size();
                assertEquals(previousDimension+1, evolvedDimension);

            } else if (effect instanceof WhiteMarbleEffect) {
                int previousDimension = personalBoard.getWhiteMarbleConversions().size();
                System.out.println(personalBoard.getWhiteMarbleConversions());
                leader.activate(personalBoard);
                System.out.println("Conversions: " + personalBoard.getWhiteMarbleConversions());
                int evolvedDimension = personalBoard.getWhiteMarbleConversions().size();
                assertEquals(previousDimension+1, evolvedDimension);

            } else if (effect instanceof ProductionEffect) {
                int previousDimension = personalBoard.getActiveProductionLeaders().size();
                leader.activate(personalBoard);
                System.out.println("ActiveProductionLeaders: " + personalBoard.getActiveProductionLeaders());
                int evolvedDimension = personalBoard.getActiveProductionLeaders().size();
                assertEquals(previousDimension+1, evolvedDimension);
            }

            assertTrue(effect.toCLIString().replaceAll("\\s","").contains(effect.getEffectSymbol()));

        }
    }

    // Verify that a leader without any requirement is always activable
    // that one without an effectively useful requirement (Resource with quantity = 0) is always activable
    // and then that another one with multiple requirements is activable only after that those requirements are satisfied
    @Test
    public void isActivableTest() throws NegativeQuantityException, WrongSettingException, SingleMatchException, LastRoundException, InvalidOperationException {
        LeaderCard freeLeader1 = new LeaderCard(new ArrayList<>(), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));
        LeaderCard freeLeader2 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 0))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

        List<Player> players = new ArrayList<>(List.of(new Player("Joe"), new Player("sara")));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        Match match1 = new MultiMatch(players);
        Player joe = match1.getPlayer("Joe");

        assertTrue(freeLeader1.isActivable(joe));
        assertTrue(freeLeader2.isActivable(joe));
        assertFalse(slotLeader.isActivable(joe));

        joe.addToStrongBox(new PhysicalResource(ResType.SHIELD, 2));
        assertFalse(slotLeader.isActivable(joe));

        joe.drawDevelopmentCard(1, CardColor.GREEN.getVal());
        joe.insertDevelopmentCard(1);
        assertFalse(slotLeader.isActivable(joe));

        joe.drawDevelopmentCard(1, CardColor.GREEN.getVal());
        joe.insertDevelopmentCard(2);
        assertTrue(slotLeader.isActivable(joe));

    }

}
