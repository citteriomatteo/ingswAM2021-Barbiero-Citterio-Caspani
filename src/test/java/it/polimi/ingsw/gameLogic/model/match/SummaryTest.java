package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.exceptions.InvalidOperationException;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.SingleMatchException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.essentials.Card;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.PlayerSummary;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.gameLogic.controller.MatchController.getKeyByValue;
import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SummaryTest extends CommonThingsTest {

    public Map<String, Card> cardMap = getCardMap(assignConfiguration("src/test/resources/TotalFreeConfiguration.json"));

    @Test
    public void updateMarketTest() throws WrongSettingException, SingleMatchException, LastRoundException, InvalidOperationException
    {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");
        Summary summary = new Summary(players, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : players)
            p.setSummary(summary);
        Match match = new MultiMatch(players);

        match.getCurrentPlayer().marketDeal(true,1);
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
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new MultiMatch(players, matchConfiguration);
        Summary summary = new Summary(match, cardMap,matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : match.getPlayers())
            p.setSummary(summary);
        Player curr = match.getCurrentPlayer();

        curr.drawDevelopmentCard(1,1);
        curr.insertDevelopmentCard(1);
        curr.drawDevelopmentCard(2,1);
        curr.insertDevelopmentCard(1);

        //bought a card in the place 2-0: checking the equals on the inverse diagonal and on the size of the modified stack
        assertEquals(getKeyByValue(cardMap, match.getCardGrid().getTop()[0][0]), summary.getCardGrid()[0][0].get(0));
        assertEquals(getKeyByValue(cardMap, match.getCardGrid().getTop()[1][0]), summary.getCardGrid()[1][0].get(0));
        assertEquals(getKeyByValue(cardMap, match.getCardGrid().getTop()[2][0]), summary.getCardGrid()[2][0].get(0));
       }

    @Test
    public void personalizedSummaryTest() throws WrongSettingException, SingleMatchException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");
        List<Player> players = new ArrayList<>(List.of(player1,player2,player3,player4));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");
        Match match = new MultiMatch(players, matchConfiguration);
        Summary summary = new Summary(match, cardMap, matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        for(Player p : match.getPlayers()) {
            p.setSummary(summary);
            summary.getPlayerSummary(p.getNickname()).updateHandLeaders(p.getHandLeaders(), cardMap);
        }

        Summary personalizedSummary = summary.personalizedSummary(player1.getNickname());
        PlayerSummary player2Summary = summary.getPlayerSummary(player2.getNickname());
        PlayerSummary obscuredPlayer2Summary = personalizedSummary.getPlayerSummary(player2.getNickname());

        assertEquals(player2Summary.getFaithMarker(), obscuredPlayer2Summary.getFaithMarker());

        assertNotEquals(player2Summary.getHandLeaders(), obscuredPlayer2Summary.getHandLeaders());

    }

}
