package it.polimi.ingsw.model.match;

import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiMatch extends Match {
    private Player currentPlayer;
    private final List<Player> players;
    private final CardGrid cardGrid;
    private int numPlayer = 0;

    /**
     * Constructor, calls the super constructor to builds the matchConfiguration, the market and the leaderStack.
     * After that it initializes the players list and the currentPlayer, builds the cardGrid and sets, for each player,
     * the personalBoard and the list of handLeader.
     * It also associates this as the match of each player in the parameter list
     * @param players the list of players in this game
     * @throws SingleMatchException if the list of players contains only one player
     * @throws WrongSettingException if are given not enough CardTypes or in the wrong order to create the cardGrid
     */
    public MultiMatch(List<Player> players) throws SingleMatchException, WrongSettingException {
        super();
        if(players.size() == 1)
            throw new SingleMatchException("This match has only one player");

        MatchConfiguration matchConfiguration = super.getMatchConfiguration();

        Collections.shuffle(players);
        this.players = players;
        this.currentPlayer = players.get(0);
        this.cardGrid = new CardGrid(matchConfiguration.getAllDevCards());
        for (int i = 0; i < 2; i++) {
            players.get(i).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 0, matchConfiguration.getBasicProduction()));
            players.get(i).setMatch(this);
            players.get(i).setHandLeaders(getLeaderStack().draw(4));
            }
        if (players.size() > 2) {
            for (int j = 2; j < players.size(); j++) {
                players.get(j).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 1, matchConfiguration.getBasicProduction()));
                players.get(j).setMatch(this);
                players.get(j).setHandLeaders(getLeaderStack().draw(4));

            }
        }
    }

    public MultiMatch(List<Player> players, MatchConfiguration matchConfiguration) throws SingleMatchException, WrongSettingException {
        super(matchConfiguration);
        if(players.size() == 1)
            throw new SingleMatchException("This match has only one player");

        this.players = players;
        this.currentPlayer = players.get(0);
        this.cardGrid = new CardGrid(matchConfiguration.getAllDevCards());
        for (int i = 0; i < 2; i++) {
            players.get(i).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 0, matchConfiguration.getBasicProduction()));
            players.get(i).setMatch(this);
            players.get(i).setHandLeaders(getLeaderStack().draw(4));
        }
        if (players.size() > 2) {
            for (int j = 2; j < players.size(); j++) {
                players.get(j).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 1, matchConfiguration.getBasicProduction()));
                players.get(j).setMatch(this);
                players.get(j).setHandLeaders(getLeaderStack().draw(4));

            }
        }
    }

    /**
     * Getter
     * @return the player whose playing in this turn
     */
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Getter
     * @return the cardGrid
     */
    @Override
    public CardGrid getCardGrid() {
        return cardGrid;
    }

    /**
     * Getter
     * @return the list of players
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method return the player who will play the next turn
     * @return the next player
     */
    public Player getNextPlayer(){
        if(numPlayer < players.size()-1)
            return players.get(numPlayer+1);
        else return players.get(0);
    }

    /**
     * This method finish the turn and change the currentPlayer
     * @return true if it worked
     */
    @Override
    public boolean nextTurn(){
        numPlayer++;
        if(numPlayer < players.size())
            currentPlayer = players.get(numPlayer);
        else
            currentPlayer = players.get(0);

        return true;
    }

    /**
     * This method returns the player with the searched nickname if it is in the players list
     * @param nickname the nickname of the searched player
     * @return the player searched if it's in the players list, or null if it isn't
     */
    @Override
    public Player getPlayer(String nickname){
        for (Player player : players) {
            if (player.getNickname().equals(nickname))
                return player;
        }

        return null;
    }
}
