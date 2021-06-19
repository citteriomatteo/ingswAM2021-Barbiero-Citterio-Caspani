package it.polimi.ingsw.model.match.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.match.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.MatchController.getKeyByValue;
import static it.polimi.ingsw.jsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerSummaryTest extends CommonThingsTest
{
    public Map<String, Card> cardMap = getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json"));

    public void setCardMap(MatchConfiguration configuration) {
        for (int i = 1; i <= configuration.getAllDevCards().size(); i++)
            cardMap.put("D" + i, configuration.getAllDevCards().get(i - 1));
        for (int i = 1; i <= configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L" + i, configuration.getAllLeaderCards().get(i - 1));
    }
    //useful in order to use the free configuration in tests
    private MatchConfiguration assignConfiguration(String config){
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();
        try {
            FileReader reader = new FileReader(config);
            return g.fromJson(reader, MatchConfiguration.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error in " + this.getClass().getSimpleName());
            System.exit(1);
            return null;
        }
    }

    @Test
    public void updateWarehouseTest() throws WrongSettingException, SingleMatchException, InvalidQuantityException, ShelfInsertException, RetryException {

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");
        setSummaries(players, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new MultiMatch(players, matchConfiguration);

        setCardMap(match.getMatchConfiguration());
        Summary summary = new Summary(match, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : match.getPlayers())
            p.setSummary(summary);

        Player curr = match.getCurrentPlayer();
        curr.getPersonalBoard().warehouseEvolution(new PhysicalResource(ResType.STONE, 2));
        curr.addToWarehouse(new PhysicalResource(ResType.COIN, 2));
        curr.moveIntoWarehouse(new PhysicalResource(ResType.COIN, 2), 2);

        curr.updateWarehouse(curr.getNickname(), curr.getPersonalBoard().getWarehouse());
        assertEquals(curr.getPersonalBoard().getWarehouse().getWarehouseDisposition(), summary.getPlayerSummary(curr.getNickname()).getWarehouse());

        curr.addToWarehouse(new PhysicalResource(ResType.STONE, 2));
        curr.moveIntoWarehouse(new PhysicalResource(ResType.STONE, 2), 4);

        curr.updateWarehouse(curr.getNickname(), curr.getPersonalBoard().getWarehouse());
        assertEquals(curr.getPersonalBoard().getWarehouse().getWarehouseDisposition(), summary.getPlayerSummary(curr.getNickname()).getWarehouse());


    }

    @Test
    public void updateStrongboxTest() throws WrongSettingException, SingleMatchException, NegativeQuantityException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration config = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");
        setSummaries(players, cardMap, config.getCustomPath(), config.getBasicProduction());
        Match match = new MultiMatch(players, config);

        setCardMap(match.getMatchConfiguration());

        Summary summary = new Summary(match, cardMap, config.getCustomPath(), config.getBasicProduction());
        for(Player p : match.getPlayers())
            p.setSummary(summary);

        Player curr = match.getCurrentPlayer();

        curr.addToStrongBox(new PhysicalResource(ResType.STONE, 5));
        curr.updateStrongbox(curr.getNickname(), curr.getPersonalBoard().getStrongBox());

        assertEquals(curr.getPersonalBoard().getStrongBox().getNumberOf(ResType.STONE),
                getNumberOf(summary.getPlayerSummary(curr.getNickname()), ResType.STONE));

        curr.addToStrongBox(new PhysicalResource(ResType.COIN, 3));
        curr.addToStrongBox(new PhysicalResource(ResType.SERVANT, 7));
        curr.updateStrongbox(curr.getNickname(), curr.getPersonalBoard().getStrongBox());


        for(ResType type : curr.getPersonalBoard().getStrongBox().getResources().keySet())
            System.out.println(type + " - " + curr.getPersonalBoard().getStrongBox().getNumberOf(type));

        //fixed positions of the resources: unknown is not present in the strongbox -> ordinal doesn't work don't know how to solve
        assertEquals(curr.getPersonalBoard().getStrongBox().getNumberOf(ResType.STONE), getNumberOf(summary.getPlayerSummary(curr.getNickname()), ResType.STONE));
        assertEquals(curr.getPersonalBoard().getStrongBox().getNumberOf(ResType.COIN), getNumberOf(summary.getPlayerSummary(curr.getNickname()), ResType.COIN));
        assertEquals(curr.getPersonalBoard().getStrongBox().getNumberOf(ResType.SERVANT), getNumberOf(summary.getPlayerSummary(curr.getNickname()), ResType.SERVANT));

    }

    @Test
    public void updateDevCardSlotsTest() throws WrongSettingException, SingleMatchException, InvalidOperationException, LastRoundException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new MultiMatch(players, matchConfiguration);

        setCardMap(match.getMatchConfiguration());
        Summary summary = new Summary(match, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : match.getPlayers())
            p.setSummary(summary);

        Player curr = match.getCurrentPlayer();

        //tests the stacking of two cards, one of lv.2 on the other of lv.1

        curr.drawDevelopmentCard(1,1);
        curr.insertDevelopmentCard(1);

        curr.updateDevCardSlots(curr.getNickname(),curr.getPersonalBoard().getDevCardSlots());
        for(int i=0; i<3; i++)
            assertEquals(curr.getPersonalBoard().getDevCardSlots().getSlots()[i].stream()
                    .map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList())
                    , summary.getPlayerSummary(curr.getNickname()).getDevCardSlots()[i]);

        curr.drawDevelopmentCard(2,1);
        curr.insertDevelopmentCard(1);

        curr.updateDevCardSlots(curr.getNickname(),curr.getPersonalBoard().getDevCardSlots());
        for(int i=0; i<3; i++)
            assertEquals(curr.getPersonalBoard().getDevCardSlots().getSlots()[i].stream()
                            .map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList())
                    , summary.getPlayerSummary(curr.getNickname()).getDevCardSlots()[i]);


    }

    @Test
    public void updateActiveLeaders() throws WrongSettingException, SingleMatchException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new MultiMatch(players, matchConfiguration);
        setCardMap(match.getMatchConfiguration());
        Summary summary = new Summary(match, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : match.getPlayers())
            p.setSummary(summary);

        //testing the equals after having activated every in-hand leader
        Player curr = player4;
        System.out.println(curr.getHandLeaders().stream().map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList()));
        for(int i = 0; i < curr.getHandLeaders().size(); i++)
            curr.activateLeader(curr.getHandLeaders().get(i));

        List<String> sortedSummaryAL = summary.getPlayerSummary(curr.getNickname()).getActiveLeaders().stream().sorted().collect(Collectors.toList());
        List<String> sortedPlayerAL = new ArrayList<>(curr.getPersonalBoard().getActiveLeaders().stream().map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList()));
        sortedPlayerAL = sortedPlayerAL.stream().sorted().collect(Collectors.toList());
        assertEquals(sortedPlayerAL, sortedSummaryAL);

    }

    //replaces the functionality of the getNumberOf() method of Strongbox's class (for the player summary).
    public int getNumberOf(PlayerSummary ps, ResType type) {
        return ps.getStrongbox().stream().filter((x)->x.getType().equals(type))
                .collect(Collectors.toList()).get(0).getQuantity();
    }

}