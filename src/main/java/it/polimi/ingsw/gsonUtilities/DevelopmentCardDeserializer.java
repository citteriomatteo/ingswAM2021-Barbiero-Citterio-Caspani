package it.polimi.ingsw.gsonUtilities;


import com.google.gson.*;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;

import java.lang.reflect.Type;
import java.util.ArrayList;
/**
@deprecated Leave this class only for reference


  This class is used to tell the gsonBuilder how to deserialize a DevelopmentCard object
 */
public class DevelopmentCardDeserializer implements JsonDeserializer<DevelopmentCard>
{
    //the JsonElement is a parsed json string that you can navigate
    @Override
    public DevelopmentCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        //in order to navigate the json string, let's consider all of this like an object with all his parameters
        JsonObject jsonObject = json.getAsJsonObject();

        //now i can use the parameters of that object for building my desired object
        try {
            //--first: create the CardType
            JsonObject jsonType = jsonObject.get("type").getAsJsonObject();
            CardType type = new CardType(CardColor.valueOf(jsonType.get("color").getAsString()),
                    jsonType.get("level").getAsInt());


            //--second: create the price by extrapolate the array
            JsonArray jsonPrice = jsonObject.getAsJsonArray("price");
            ArrayList<PhysicalResource> price = deserializeArray(jsonPrice);


            //--third: create the production by extrapolate the two arrays contained inside and than create object by object
            JsonObject jsonProduction = jsonObject.get("production").getAsJsonObject();
            //create the cost list
            JsonArray jsonSentCost = jsonProduction.getAsJsonArray("sentCost");
            ArrayList<PhysicalResource> sentCost = deserializeArray(jsonSentCost);

            //create the earnings list
            JsonArray jsonEarnings = jsonProduction.getAsJsonArray("earnings");
            ArrayList<Resource> earnings = new ArrayList<>();
            //for each element of the list
            for (int i = 0; i < jsonEarnings.size(); i++) {
                JsonObject jsonResource = jsonEarnings.get(i).getAsJsonObject();
                //control if the resource is a faithPoint or a PhysicalResource
                if(jsonResource.get("faithPoint").getAsBoolean()) {
                    earnings.add(new FaithPoint(jsonResource.get("quantity").getAsInt()));
                }else {
                    earnings.add(new PhysicalResource(ResType.valueOf(jsonResource.get("type").getAsString()),
                            jsonResource.get("quantity").getAsInt()));
                }
            }

            //the winPoints can be passed directly into the return
            return new DevelopmentCard(type, price, new Production(sentCost, earnings), jsonObject.get("winPoints").getAsInt());



        } catch (InvalidQuantityException e) {
            System.err.println("Problem in parameters of json file elements");
            e.printStackTrace();
        }
        return null;
    }

    //this function deserialize element by element the array of PhysicalResources
    private ArrayList<PhysicalResource> deserializeArray(JsonArray jsonArray) throws NegativeQuantityException {
        ArrayList<PhysicalResource> res = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonPhysicalResource = jsonArray.get(i).getAsJsonObject();
            res.add(new PhysicalResource(ResType.valueOf(jsonPhysicalResource.get("type").getAsString()),
                    jsonPhysicalResource.get("quantity").getAsInt()));
        }

        return res;
    }
}

