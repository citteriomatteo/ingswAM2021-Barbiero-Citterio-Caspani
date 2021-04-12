package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.gsonUtilities.GsonHandler;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Match implements Comunicator{
    private Player currentPlayer;
    private final List<Player> players;
    private final Market market;
    private final CardGrid cardGrid;
 //   private final LeaderStack leaderStack;
    private int numPlayer = 0;


    public Match(List<Player> players) throws WrongSettingException, FileNotFoundException {
        String filePath = "src/test/resources/CardGridExample.json";
        Gson g = GsonHandler.resourceConfig(new GsonBuilder()).create();
        FileReader reader = new FileReader(filePath);

        Type collectionType = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
        ArrayList<DevelopmentCard> extractedJson = g.fromJson(reader, collectionType);

        this.currentPlayer = players.get(0);
        this.players = players;
        this.market = new Market();
        this.cardGrid = new CardGrid(extractedJson);
        //TODO leader stack need a list of LeaderCards
       // this.leaderStack = new LeaderStack(null);
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

 /*   public LeaderStack getLeaderStack() {
        return leaderStack;
    }
*/
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
