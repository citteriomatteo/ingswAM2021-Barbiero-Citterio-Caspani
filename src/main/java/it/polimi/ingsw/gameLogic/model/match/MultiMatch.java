package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.exceptions.SingleMatchException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.PersonalBoard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class extends common things defined in Match class with multiplayer features.
 */
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
            players.get(i).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 0, matchConfiguration.getBasicProduction(), players.get(i)));
            players.get(i).setMatch(this);
            players.get(i).setInitialHandLeaders(getLeaderStack().draw(4));
            }
        if (players.size() > 2) {
            for (int j = 2; j < players.size(); j++) {
                players.get(j).setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), 1, matchConfiguration.getBasicProduction(), players.get(j)));
                players.get(j).setMatch(this);
                players.get(j).setInitialHandLeaders(getLeaderStack().draw(4));

            }
        }
        adminCase();
    }

    /**
     * This constructor is the one used in case of a custom configuration's multiplayer match.
     * @param players the list of players
     * @param matchConfiguration the custom configuration
     * @throws SingleMatchException when the list is composed only by one player (must be at least 2).
     * @throws WrongSettingException when some configuration settings are wrong.
     */
    public MultiMatch(List<Player> players, MatchConfiguration matchConfiguration) throws SingleMatchException, WrongSettingException {
        super(matchConfiguration);
        if(players.size() == 1)
            throw new SingleMatchException("This match has only one player");

        Collections.shuffle(players);
        this.players = players;
        this.currentPlayer = players.get(0);
        this.cardGrid = new CardGrid(matchConfiguration.getAllDevCards());

        ArrayList<Cell> customPath = (ArrayList<Cell>) matchConfiguration.getCustomPath();
        for (int i = 0; i < 2; i++) {
            players.get(i).setPersonalBoard(new PersonalBoard(customPath, 0, matchConfiguration.getBasicProduction(), players.get(i)));
            players.get(i).setMatch(this);

            players.get(i).setInitialHandLeaders(getLeaderStack().draw(4));
        }
        if (players.size() > 2) {
            for (int j = 2; j < players.size(); j++) {
                players.get(j).setPersonalBoard(new PersonalBoard(customPath, 1, matchConfiguration.getBasicProduction(), players.get(j)));
                players.get(j).setMatch(this);
                players.get(j).setInitialHandLeaders(getLeaderStack().draw(4));

            }
        }
        adminCase();
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

        int position = (numPlayer+1)%(players.size());
        return players.get(position);
    }

    /**
     * Sets the state of the last currentPlayer as WAITING_FOR_TURN.
     * Then if all the players in the match are disconnected returns STARTING_TURN and finishes, else calls itself while reaches a connected player.
     * When it founds a connected player gets the last used state of that player and then evolves the player's state.
     * @return the state of the current player.
     */
    @Override
    public StateName nextTurn(){
        StateName state;
        if(currentPlayer.isConnected())
            currentPlayer.updateLastUsedState(currentPlayer.getNickname(), StateName.WAITING_FOR_TURN);

        numPlayer = (numPlayer+1)%(players.size());
        currentPlayer = players.get(numPlayer);

        if(!currentPlayer.isConnected()) {
            if(allPlayersAreDisconnected())
                return currentPlayer.getSummary().getPlayerSummary(currentPlayer.getNickname()).getLastUsedState();
            state = this.nextTurn();
        }
        else{
            StateName prevState = currentPlayer.getSummary().getPlayerSummary(currentPlayer.getNickname()).getLastUsedState();
            if (prevState == StateName.END_TURN || prevState == StateName.WAITING_FOR_TURN) {
                currentPlayer.updateLastUsedState(currentPlayer.getNickname(), StateName.STARTING_TURN);
                return StateName.STARTING_TURN;
            }
            else {
                currentPlayer.updateLastUsedState(currentPlayer.getNickname(), prevState);
                return prevState;
            }
        }

        return state;
    }

    /**
     * Returns true if all the players are disconnected
     * @return true if all the players are disconnected
     */
    private boolean allPlayersAreDisconnected() {
        for(Player p : players)
            if(p.isConnected())
                return false;

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
