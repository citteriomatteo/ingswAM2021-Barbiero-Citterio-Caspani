package it.polimi.ingsw.gsonUtilities;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.*;

/*
 Class that expose static methods used for better build the GsonBuilder,
 to create the Gson object just call on the returned object .create()
 */
public class GsonHandler {

    // Returns a gson builder ready to parse an object that implements Resource Interface
    public static GsonBuilder resourceConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Resource.class, "resource")
                .registerSubtype(PhysicalResource.class, "physicalResource")
                .registerSubtype(FaithPoint.class, "faithPoint"));
    }

    // Returns a gson builder ready to parse an object that implements Effect Interface
    public static GsonBuilder effectConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Effect.class, "name")
                .registerSubtype(DiscountEffect.class, "discountEffect")
                .registerSubtype(ProductionEffect.class, "productionEffect")
                .registerSubtype(SlotEffect.class, "slotEffect")
                .registerSubtype(WhiteMarbleEffect.class, "whiteMarbleEffect"));
    }

    // Returns a gson builder ready to parse an object that implements Requirable Interface
    public static GsonBuilder requirableConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Requirable.class, "requirement")
                .registerSubtype(PhysicalResource.class, "physicalResource")
                .registerSubtype(CardType.class, "cardType"));
    }

}
