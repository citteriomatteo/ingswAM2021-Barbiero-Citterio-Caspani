package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;

public abstract class Match implements Comunicator{
    private final Market market;
    private final LeaderStack leaderStack;
    private final MatchConfiguration matchConfiguration;



    public Match(String config) throws WrongSettingException, FileNotFoundException, NegativeQuantityException {
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();
        FileReader reader = new FileReader(config);

        matchConfiguration = g.fromJson(reader, MatchConfiguration.class);

        this.market = new Market();
        this.leaderStack = new LeaderStack(matchConfiguration.getAllLeaderCards());
    }


    public Market getMarket() {
        return market;
    }

    public LeaderStack getLeaderStack() {
        return leaderStack;
    }

    public MatchConfiguration getMatchConfiguration() {
        return matchConfiguration;
    }

    public abstract boolean nextTurn() throws NegativeQuantityException, MatchEndedException;

    public abstract CardGrid getCardGrid();



}
