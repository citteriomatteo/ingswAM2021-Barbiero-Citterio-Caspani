package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Verificator;

import java.util.ArrayList;
import java.util.List;

/*
Represents a production with its cost and earnings
 */
public class Production {
    private final List<PhysicalResource> cost;
    private final List<Resource> earnings;

    /*
    Simple constructor, if the cost list contains multiple occurrences of the same ResType
    the constructor reformat the list in the correct way
     */
    public Production(List<PhysicalResource> sentCost, List<Resource> earnings) throws NegativeQuantityException {
        this.cost = new ArrayList<>();
        int count;
        for(ResType type : ResType.values()){
            count = 0;
            for (PhysicalResource tempRes : sentCost) {
                if (tempRes.getType().equals(type)) {
                    count += tempRes.getQuantity();
                }
            }
            if (count!=0)
                cost.add(new PhysicalResource(type, count));

        }
        this.earnings = earnings;
    }

    public List<PhysicalResource> getCost() {
        return cost;
    }

    public List<Resource> getEarnings() {
        return earnings;
    }

    //Adds the earnings without paying the costs, they have to be paid before anywhere else
    public boolean produce(Adder adder) throws InvalidAddFaithException {
        for (Resource resource : earnings){
            resource.add(adder);
        }

        return true;
    }

    //Returns true if you have the resource to activate this production
    public boolean isPlayable(Verificator verificator){
        for (PhysicalResource resource: cost){
          if(!resource.verify(verificator))
              return false;
        }

        return true;

    }

}