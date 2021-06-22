package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.model.match.market.Market;
import it.polimi.ingsw.gameLogic.model.match.player.Player;

import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;

/**
 * This class implements the Match functionalities.
 */
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

    /**
     * Custom constructor, for initializing a match after a custom configuration creation.
     * @param matchConfiguration the configuration chosen
     */
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

    /**
     * Controls if there is a player nickname starting for admin and charge his strongbox for easy testing
     */
    protected void adminCase(){
        for(Player p : this.getPlayers())
            if(p.getNickname().startsWith("admin"))
                chargeStrongBox(p, 50);
    }

    /**
     * Charge the strongbox of the relative player with value of every resource
     * @param p the player you want to charge the strongbox to
     * @param value the quantity of resources you want to put in the strongbox for every type
     */
    private void chargeStrongBox(Player p, int value){
        Arrays.stream(ResType.values()).filter((x)->!x.equals(ResType.UNKNOWN)).forEach((x)-> p.addToStrongBox(new PhysicalResource(x, value)));
    }

}
