package it.polimi.ingsw.gsonUtilities;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.essentials.FaithPoint;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Resource;

/*
Class that expose static methods used for better build the GsonBuilder,
 to create the Gson object just call on the returned object .create()
 */
public class GsonHandler {

    //Returns a gson builder ready to parse an object that implements Resource Interface
    public static GsonBuilder resourceConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Resource.class, "resource")
                .registerSubtype(PhysicalResource.class, "physicalResource")
                .registerSubtype(FaithPoint.class, "faithPoint"));
    }

}
