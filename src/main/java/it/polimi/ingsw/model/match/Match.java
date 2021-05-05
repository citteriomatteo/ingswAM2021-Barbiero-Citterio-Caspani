package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;

public abstract class Match implements Communicator {
    private final Market market;
    private final LeaderStack leaderStack ;
    private final MatchConfiguration matchConfiguration;

    /**
     * Constructor, builds the common parts to MultiMatch and SingleMatch (matchConfiguration, Market and LeaderStack)
     * from a json file
     * @param config the file path of the configuration file
     */
    public Match(String config){
            matchConfiguration = assignConfiguration(config);
            this.market = new Market();
            this.leaderStack = new LeaderStack(matchConfiguration.getAllLeaderCards());
    }

    /**
     * Internal function used to read the configuration from the json file at 'config' filePath
     * @param config the file path of the configuration file
     * @return the object read in the json
     */
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


    /**
     * Getter
     * @return market
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Getter
     * @return leaderStack
     */
    public LeaderStack getLeaderStack() {
        return leaderStack;
    }

    /**
     * Getter
     * @return matchConfiguration
     */
    public MatchConfiguration getMatchConfiguration() {
        return matchConfiguration;
    }

    /**
     * Getter, returns a CardGrid or a singleCardGrid
     * @return cardGrid
     */
    public abstract CardGrid getCardGrid();

    /**
     * Getter
     * @return the list of players
     */
    public abstract List<Player> getPlayers();

    /**
     * Getter
     * @return the player who is playing in this turn
     */
    public abstract Player getCurrentPlayer();

    /**
     * This method finish the turn
     * @return true if it worked
     * @throws LastRoundException if the number of a certain type of developmentCards became 0 in single player
     */
    public abstract boolean nextTurn() throws LastRoundException;

    /**
     * This method returns the player with the searched nickname if it is in the players list
     * @param nickname the nickname of the searched player
     * @return the player searched if it's in the players list, or null if it isn't
     */
    public abstract Player getPlayer(String nickname);





}
