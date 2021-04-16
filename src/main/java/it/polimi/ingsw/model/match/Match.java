package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;

public abstract class Match implements Comunicator{
    private final Market market;
    private final LeaderStack leaderStack;
    private final MatchConfiguration matchConfiguration;

    /**
     * Constructor, builds the common parts to MultiMatch and SingleMatch (matchConfiguration, Market and LeaderStack)
     * from a json file
     * @param config the directory of the configuration file
     * @throws FileNotFoundException if it can't reade the configuration file
     */

    public Match(String config) throws FileNotFoundException {
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();
        FileReader reader = new FileReader(config);

        matchConfiguration = g.fromJson(reader, MatchConfiguration.class);

        this.market = new Market();
        this.leaderStack = new LeaderStack(matchConfiguration.getAllLeaderCards());
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
     * @return the player whose playing in this turn
     */

    public abstract Player getCurrentPlayer();

    /**
     * This method finish the turn
     * @return true if it worked
     * @throws MatchEndedException if the number of a certain type of developmentCards became 0
     */

    public abstract boolean nextTurn() throws MatchEndedException;

    /**
     * This method returns the player with the searched nickname if it is in the players list
     * @param nickname the nickname of the searched player
     * @return the player searched if it's in the players list, or null if it isn't
     */

    public abstract Player getPlayer(String nickname);





}
