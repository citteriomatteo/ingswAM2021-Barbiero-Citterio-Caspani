package it.polimi.ingsw.model.match;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;

import java.util.List;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;

public abstract class Match implements Communicator {
    private final Market market;
    private final LeaderStack leaderStack ;
    private final MatchConfiguration matchConfiguration;

    /**
     * Constructor, builds the common parts to MultiMatch and SingleMatch (matchConfiguration, Market and LeaderStack)
     * from a json file
     */
    public Match(){
            matchConfiguration = assignConfiguration("src/main/resources/json/StandardConfiguration.json");
            this.market = new Market();
            this.leaderStack = new LeaderStack(matchConfiguration.getAllLeaderCards());
    }

    //todo JAVADOC
    public Match(MatchConfiguration matchConfiguration){
        this.matchConfiguration = matchConfiguration;
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
     * @return the player who is playing in this turn
     */
    public abstract Player getCurrentPlayer();

    /**
     * This method finish the turn
     * @return true if it worked
     * @throws LastRoundException if the number of a certain type of developmentCards became 0 in single player
     */
    public abstract StateName nextTurn() throws LastRoundException;

    /**
     * This method returns the player with the searched nickname if it is in the players list
     * @param nickname the nickname of the searched player
     * @return the player searched if it's in the players list, or null if it isn't
     */
    public abstract Player getPlayer(String nickname);

}
