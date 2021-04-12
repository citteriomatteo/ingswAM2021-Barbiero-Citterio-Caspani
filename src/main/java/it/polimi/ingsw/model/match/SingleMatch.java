package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.token.TokenStack;

import java.io.FileNotFoundException;
import java.util.List;

public class SingleMatch extends Match{
    private SingleCardGrid singleCardGrid;
    private TokenStack tokenStack;


    public SingleMatch(List<Player> players, String config) throws FileNotFoundException, WrongSettingException, NegativeQuantityException {
        super(players,config);

        this.singleCardGrid = new SingleCardGrid(super.getMatchConfiguration().getAllDevCards());
        this.tokenStack = new TokenStack();
    }

    public SingleCardGrid getSingleCardGrid() {
        return singleCardGrid;
    }

    public TokenStack getTokenStack() {
        return tokenStack;
    }


}
