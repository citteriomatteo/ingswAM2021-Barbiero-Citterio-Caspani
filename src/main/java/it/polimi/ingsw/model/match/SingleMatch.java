package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.gsonUtilities.GsonHandler;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.token.TokenStack;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SingleMatch extends Match{
    private SingleCardGrid singleCardGrid;
    private TokenStack tokenStack;


    public SingleMatch(List<Player> players) throws FileNotFoundException, WrongSettingException {
        super(players);
        String filePath = "src/test/resources/CardGridExample.json";
        Gson g = GsonHandler.resourceConfig(new GsonBuilder()).create();
        FileReader reader = new FileReader(filePath);
        Type collectionType = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
        ArrayList<DevelopmentCard> extractedJson = g.fromJson(reader, collectionType);


        this.singleCardGrid = new SingleCardGrid(extractedJson);
        this.tokenStack = new TokenStack();
    }

    public SingleCardGrid getSingleCardGrid() {
        return singleCardGrid;
    }

    public TokenStack getTokenStack() {
        return tokenStack;
    }


}
