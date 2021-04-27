package it.polimi.ingsw.model.match.player.personalBoard;

import java.util.*;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NotEnoughResourcesException;

public class StrongBox
{
    private Map<ResType, Integer> resources;

    /** The constructor initialize the empty map, with all the available Resource types put at 0 quantity. */
    public StrongBox()
    {
        resources = new HashMap<>();
        for(ResType type: ResType.values())
            resources.put(type, 0);
    }

    /** @return the Strongbox map */
    public Map<ResType, Integer> getResources() { return resources; }

    /**
     * This method adds the received resource into the strongbox.
     * @param res is the resource to put
     * @return    true
     */
    public boolean put(PhysicalResource res)
    {
        resources.replace(res.getType(), (resources.get(res.getType()) + res.getQuantity()));
        return true;
    }

    /**
     * This method takes the resources from the strongbox, if present.
     * @param res indicates the resource to take
     * @return    the same "res" resource, taken from the strongbox
     * @throws NotEnoughResourcesException if the chosen resources are not present in the strongbox
     */
    public PhysicalResource take(PhysicalResource res) throws NotEnoughResourcesException
    {
        if(resources.get(res.getType()) < res.getQuantity())
            throw new NotEnoughResourcesException("Not enough "+res.getType().toString()+" in the StrongBox!");
        else
            resources.replace(res.getType(), (resources.get(res.getType()) - res.getQuantity()));
        return res;
    }

    /**
     * This method returns the quantity of the specified resource.
     * @param type is the type to search
     * @return     the HashMap get-end flag
     */
    public int getNumberOf(ResType type) { return resources.get(type); }

    public static StrongBox clone(StrongBox sb){
        StrongBox clone = null;
        try {
            clone = (StrongBox) sb.clone();
        }
        catch(CloneNotSupportedException e){ e.printStackTrace(); }
        return clone;
    }

}
