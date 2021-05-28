package it.polimi.ingsw.jsonUtilities;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;

import static it.polimi.ingsw.jsonUtilities.GsonHandler.*;

public class JsonParser implements Parser{
    private static final JsonParser instance = new JsonParser();
    private final Gson parserCtoS;
    private final Gson parserStoC;
    private final Gson parserMatchObjects;

    public JsonParser() {
        parserCtoS = cToSMessageConfig(new GsonBuilder()).create();
        parserStoC = sToCMessageConfig(new GsonBuilder()).create();
        parserMatchObjects = completeModelConfig(new GsonBuilder()).create();
    }

    public static JsonParser getParser(){
        return instance;
    }

}
