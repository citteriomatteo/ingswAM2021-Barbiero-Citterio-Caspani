package it.polimi.ingsw.model.match;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.token.TokenStack;
import it.polimi.ingsw.network.message.stocmessage.TokenDrawMessage;

import java.util.ArrayList;
import java.util.List;

public class SingleMatch extends Match{
    private final Player currentPlayer;
    private final SingleCardGrid singleCardGrid;
    private final TokenStack tokenStack;

    /**
     * Constructor, calls the super constructor to builds the matchConfiguration, the market and the leaderStack.
     * After that it initializes the currentPlayer, builds the singleCardGrid, the tokenStack and sets
     * the personalBoard and the list of handLeader of the singlePlayer.
     * It also associates this as the match of the singlePlayer.
     * @param player the player of this match
     * @throws WrongSettingException if are given not enough CardTypes or in the wrong order to create the singleCardGrid
     */
    public SingleMatch(Player player) throws WrongSettingException
    {
        super();
        this.currentPlayer = player;
        MatchConfiguration matchConfiguration = super.getMatchConfiguration();
        this.singleCardGrid = new SingleCardGrid(matchConfiguration.getAllDevCards());
        this.tokenStack = new TokenStack();
        currentPlayer.setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction(), currentPlayer));
        currentPlayer.setMatch(this);
        currentPlayer.setInitialHandLeaders(getLeaderStack().draw(4));

        adminCase();

    }

    //todo JAVADOC
    public SingleMatch(Player player, MatchConfiguration config) throws WrongSettingException {
        super(config);
        this.currentPlayer = player;
        this.singleCardGrid = new SingleCardGrid(config.getAllDevCards());
        this.tokenStack = new TokenStack();
        currentPlayer.setPersonalBoard(new PersonalBoard((ArrayList<Cell>) config.getCustomPath(), config.getBasicProduction(), currentPlayer));
        currentPlayer.setMatch(this);
        currentPlayer.setInitialHandLeaders(getLeaderStack().draw(4));

        adminCase();
    }

    /**
     * Getter
     * @return the tokenStack
     */
    public TokenStack getTokenStack() {
        return tokenStack;
    }

    /**
     * Getter
     * @return the only player
     */
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Getter
     * @return the singleCardGrid
     */
    @Override
    public CardGrid getCardGrid() {
        return singleCardGrid;
    }

    /**
     * Getter
     * @return the single player as list
     */
    @Override
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(List.of(currentPlayer));
    }

    /**
     * This method returns the player with the searched nickname if it is in the player of this match, else return null
     * @param nickname the nickname of the searched player
     * @return the player searched if it's the player of this match, or null if it isn't
     */
    @Override
    public Player getPlayer(String nickname){
        if (currentPlayer.getNickname().equals(nickname))
            return currentPlayer;
        else
            return null;
    }

    /**
     * This method finish the turn by calling the draw method in tokenStack
     * @return true if the draw method worked
     * @throws LastRoundException if the number of a certain type of developmentCards became 0
     */
    @Override
    public StateName nextTurn() throws LastRoundException {

        currentPlayer.updateLastUsedState(currentPlayer.getNickname(), StateName.WAITING_FOR_TURN);
        String drawnToken = tokenStack.getStack().peek().toString();
        tokenStack.draw(this);

        new TokenDrawMessage("", drawnToken, tokenStack.getStack().size()).sendBroadcast(this);

        currentPlayer.updateLastUsedState(currentPlayer.getNickname(), StateName.STARTING_TURN);
        return StateName.STARTING_TURN;
    }

}
