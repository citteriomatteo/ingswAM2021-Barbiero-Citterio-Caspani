package it.polimi.ingsw.model.match.player.personalBoard;

import java.util.*;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NotEnoughResourcesException;

public class StrongBox
{
    private Map<ResType, Integer> resources;

    //Constructor initialize the empty map.
    public StrongBox()
    {
        resources = new HashMap<>();
        for(ResType type: ResType.values())
            resources.put(type, 0);
    }

    public Map<ResType, Integer> getResources() { return resources; }

    /*
    This method adds the received resource into the strongbox.
    Launches an exception if something goes wrong.
     */
    public boolean put(PhysicalResource res)
    {
        resources.replace(res.getType(), (resources.get(res.getType()) + res.getQuantity()));
        return true;
    }

    /*
    This method takes the resources from the strongbox, if present.
    Otherwise, the operation throws an exception.
     */
    public PhysicalResource take(PhysicalResource res) throws NotEnoughResourcesException
    {
        if(resources.get(res.getType()) < res.getQuantity())
            throw new NotEnoughResourcesException("Not enough "+res.getType().toString()+" in the StrongBox!");
        else
            resources.replace(res.getType(), (resources.get(res.getType()) - res.getQuantity()));
        return res;
    }

    //Returns the quantity of the specified resource.
    public int getNumberOf(ResType type) { return resources.get(type); }


}
