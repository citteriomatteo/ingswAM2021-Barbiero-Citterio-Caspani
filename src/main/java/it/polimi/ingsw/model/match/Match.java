package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;

public class Match implements Comunicator{
    private Player currentPlayer;
    private final List<Player> players;
    private final Market market;
    private final CardGrid cardGrid;
    private final LeaderStack leaderStack;
    private final MatchConfiguration matchConfiguration;
    private int numPlayer = 0;


    public Match(List<Player> players, String config) throws WrongSettingException, FileNotFoundException, NegativeQuantityException {
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();
        FileReader reader = new FileReader(config);


        matchConfiguration = g.fromJson(reader, MatchConfiguration.class);

        this.currentPlayer = players.get(0);
        this.players = players;
        this.market = new Market();
        this.cardGrid = new CardGrid(matchConfiguration.getAllDevCards());
        this.leaderStack = new LeaderStack(matchConfiguration.getAllLeaderCards());
        if(players.size() == 1)
            currentPlayer.setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction()));
        else {
            for (int i = 0; i < 2; i++) {
                players.get(i).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 0, matchConfiguration.getBasicProduction()));
                if(players.size() > 2) {
                    for (int j = 2; j < players.size(); j++) {
                        players.get(i).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 1, matchConfiguration.getBasicProduction()));

                    }
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Market getMarket() {
        return market;
    }

    public CardGrid getCardGrid() {
        return cardGrid;
    }

    public LeaderStack getLeaderStack() {
        return leaderStack;
    }

    public MatchConfiguration getMatchConfiguration() {
        return matchConfiguration;
    }

    public Player getNextPlayer(){
        if(numPlayer < players.size()-1)
            return players.get(numPlayer+1);
        else return players.get(0);
    }

    public boolean nextTurn(){
        numPlayer++;
        if(numPlayer < players.size()-1)
            currentPlayer = players.get(numPlayer);
        else
            currentPlayer = players.get(0);

        return true;
    }
}
