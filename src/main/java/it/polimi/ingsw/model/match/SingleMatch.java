package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.token.TokenStack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SingleMatch extends Match{
    private final Player currentPlayer;
    private Market market;
    private LeaderStack leaderStack;
    private final MatchConfiguration matchConfiguration;
    private final SingleCardGrid singleCardGrid;
    private final TokenStack tokenStack;


    public SingleMatch(Player player, String config) throws FileNotFoundException, WrongSettingException, NegativeQuantityException {
        super(config);
        matchConfiguration = super.getMatchConfiguration();

        this.currentPlayer = player;
        this.singleCardGrid = new SingleCardGrid(matchConfiguration.getAllDevCards());
        this.tokenStack = new TokenStack();
        currentPlayer.setPersonalBoard(new PersonalBoard((ArrayList<Cell>) super.getMatchConfiguration().getCustomPath(), super.getMatchConfiguration().getBasicProduction()));
        currentPlayer.setMatch(this);


    }


    public TokenStack getTokenStack() {
        return tokenStack;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CardGrid getCardGrid() {
        return singleCardGrid;
    }

    @Override
    public boolean nextTurn() throws NegativeQuantityException, MatchEndedException {
        return tokenStack.draw(this);
    }

    @Override
    public List<Player> getPlayers() {
        return List.of(currentPlayer);
    }
}
