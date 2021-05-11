package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.gsonUtilities.GsonHandler.effectConfig;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class SummaryTest {

    public Map<String, Card> cardMap = new HashMap<>();

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
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    @Test
    public void updateMarketTest() throws WrongSettingException, SingleMatchException, LastRoundException, InvalidOperationException
    {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        Match match = new MultiMatch(new ArrayList<>(List.of(player1,player2,player3,player4)));
        Summary summary = new Summary(match, cardMap);
        for(Player p : match.getPlayers())
            p.setSummary(summary);

        match.getCurrentPlayer().marketDeal(true,0);
        match.getCurrentPlayer().updateMarket(match.getMarket());

        //checking the equals on the slide marble and on the main diagonal:
        assertEquals(Character.toLowerCase(match.getMarket().getSlide().toString().charAt(0)), summary.getSideMarble());
        assertEquals(Character.toLowerCase(match.getMarket().getBoard()[0][0].toString().charAt(0)), summary.getMarket()[0][0]);
        assertEquals(Character.toLowerCase(match.getMarket().getBoard()[1][1].toString().charAt(0)), summary.getMarket()[1][1]);
        assertEquals(Character.toLowerCase(match.getMarket().getBoard()[2][2].toString().charAt(0)), summary.getMarket()[2][2]);


    }

    @Test
    public void updateCardGrid() throws WrongSettingException, SingleMatchException, InvalidOperationException, LastRoundException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        Match match = new MultiMatch(new ArrayList<>(List.of(player1,player2,player3,player4)), assignConfiguration("src/test/resources/TotalFreeConfiguration.json"));
        setCardMap(match.getMatchConfiguration());
        Summary summary = new Summary(match, cardMap);
        for(Player p : match.getPlayers())
            p.setSummary(summary);
        Player curr = match.getCurrentPlayer();

        curr.drawDevelopmentCard(1,1);
        curr.insertDevelopmentCard(1);
        curr.drawDevelopmentCard(2,1);
        curr.insertDevelopmentCard(1);
        curr.updateDevCardSlots(curr.getNickname(), curr.getPersonalBoard().getDevCardSlots());

        //bought a card in the place 2-0: checking the equals on the inverse diagonal and on the size of the modified stack
        assertEquals(getKeyByValue(cardMap, match.getCardGrid().getTop()[0][2]), summary.getCardGrid()[0][2].get(0));
       }

}