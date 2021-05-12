package it.polimi.ingsw.model.match;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;

import java.util.*;

public class SummaryTest extends CommonThingsTest {

    public Map<String, Card> cardMap = getCardMap(assignConfiguration("src/test/resources/TotalFreeConfiguration.json"));

    public void setCardMap(MatchConfiguration configuration) {
        for (int i = 1; i <= configuration.getAllDevCards().size(); i++)
            cardMap.put("D" + i, configuration.getAllDevCards().get(i - 1));
        for (int i = 1; i <= configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L" + i, configuration.getAllLeaderCards().get(i - 1));
    }

    @Test
    public void updateMarketTest() throws WrongSettingException, SingleMatchException, LastRoundException, InvalidOperationException
    {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        Summary summary = new Summary(players, cardMap);
        for(Player p : players)
            p.setSummary(summary);
        Match match = new MultiMatch(players);

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
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        setSummaries(players, cardMap);
        Match match = new MultiMatch(players, assignConfiguration("src/test/resources/TotalFreeConfiguration.json"));
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
