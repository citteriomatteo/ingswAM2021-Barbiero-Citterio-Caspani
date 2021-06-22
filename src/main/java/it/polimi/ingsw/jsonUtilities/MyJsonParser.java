package it.polimi.ingsw.jsonUtilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.*;

/**
 * Implementation of the Parser interface in order to use Json language, it is a eager singleton,
 * you can recall it using {@link MyJsonParser#getParser()}
 */
public class MyJsonParser implements Parser{
    private static final MyJsonParser instance = new MyJsonParser();
    private final Gson parserCtoS;
    private final Gson parserStoC;
    private final Gson parserMatchObjects;

    /**
     * Private constructor of the singleton, it creates the three different Gson objects
     * necessary for different parsing purposes
     */
    private MyJsonParser() {
        parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
        parserStoC = sToCMessageConfig(new GsonBuilder()).create();
        parserMatchObjects = completeModelConfig(new GsonBuilder()).create();
    }

    /**
     * @return the instance of the JsonParser singleton
     */
    public static MyJsonParser getParser() {
        return instance;
    }

    @Override
    public String parseFromCtoSMessage(CtoSMessage message) {
        return parserCtoS.toJson(message, CtoSMessage.class);
    }

    @Override
    public CtoSMessage parseInCtoSMessage(String message) {
        return parserCtoS.fromJson(message, CtoSMessage.class);
    }

    @Override
    public String parseFromStoCMessage(StoCMessage message) {
        return parserStoC.toJson(message, StoCMessage.class);
    }

    @Override
    public StoCMessage parseInStoCMessage(String message) {
        return parserStoC.fromJson(message, StoCMessage.class);
    }

    @Override
    public MatchConfiguration readMatchConfiguration(@NotNull Reader reader) {
        return parserMatchObjects.fromJson(reader, MatchConfiguration.class);
    }
}
