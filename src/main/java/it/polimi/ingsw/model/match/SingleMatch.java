package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.token.TokenStack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SingleMatch extends Match{
    private final Player currentPlayer;
    private final SingleCardGrid singleCardGrid;
    private final TokenStack tokenStack;


    public SingleMatch(Player player, String config) throws FileNotFoundException, WrongSettingException
    {
        super(config);
        this.currentPlayer = player;
        MatchConfiguration matchConfiguration = super.getMatchConfiguration();
        this.singleCardGrid = new SingleCardGrid(matchConfiguration.getAllDevCards());
        this.tokenStack = new TokenStack();
        currentPlayer.setPersonalBoard(new PersonalBoard((ArrayList<Cell>) matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction()));
        currentPlayer.setMatch(this);


    }


    public TokenStack getTokenStack() {
        return tokenStack;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    @Override
    public CardGrid getCardGrid() {
        return singleCardGrid;
    }

    @Override
    public boolean nextTurn() throws MatchEndedException {
        return tokenStack.draw(this);
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(List.of(currentPlayer));
    }
}
