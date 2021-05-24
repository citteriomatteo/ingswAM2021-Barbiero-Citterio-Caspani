package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Verificator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a production with its cost and earnings
 */
public class Production {
    private final List<PhysicalResource> cost;
    private final List<Resource> earnings;

    /**
     * Class constructor, if the cost list contains multiple occurrences of the same ResType
     * the constructor reformat the list in the correct way
     * @param sentCost the costs due to pay to activate this production
     * @param earnings the earnings derived by the production of this. The list can contain PhysicalResources and FaithPoints
     */
    public Production(List<PhysicalResource> sentCost, List<Resource> earnings) {
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
                try { cost.add(new PhysicalResource(type, count)); }
                catch(NegativeQuantityException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); System.exit(1);}

        }
        this.earnings = earnings;
    }

    /**
     * Copy constructor, generates an identical production to the one passed
     * @param toCopy the production to copy
     */
    public Production(Production toCopy){
        cost = new ArrayList<>(toCopy.cost);
        earnings = new ArrayList<>(toCopy.earnings);
    }

    /**
     * getter
     * @return the cost due to pay to activate this production
     */
    public List<PhysicalResource> getCost() {
        return cost;
    }
    /**
     * getter
     * @return the earnings derived by the production of this
     */
    public List<Resource> getEarnings() {
        return earnings;
    }

    /**
     * Adds the earnings of this to the StrongBox or the FaitPath of the player.
     * This method doesn't control if the costs had been already payed.
     * @requires the costs have to be paid by the player before anywhere else
     * @param adder the player whom the earnings has to be added to
     * @return true
     * @throws LastRoundException if this production make the player reach the end of his FaithPath
     */
    public boolean produce(Adder adder) throws LastRoundException {
        LastRoundException exc = null;
        for (Resource resource : earnings){
            try {
                resource.add(adder);
            }
            catch (LastRoundException e) { exc = e; }
        }
        if(exc != null)
            throw exc;

        return true;
    }

    /**
     * Returns true if you have the resource to activate this production
     * @param verificator the player who wants to activate this production
     * @return true if you have the resource to activate this production
     */
    public boolean isPlayable(Verificator verificator){
        List<PhysicalResource> discountedCosts = verificator.getDiscountedCosts(cost);

        for (PhysicalResource resource: discountedCosts){
          if(!resource.verify(verificator))
              return false;
        }

        return true;

    }

    /**
     * Clone this Production, same as {@link Production#Production(Production)}
     * @return a new identical production
     */
    public Production clone(){
        return new Production(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(getCost(), that.getCost()) && Objects.equals(getEarnings(), that.getEarnings());
    }


    @Override
    public String toString() {
        return "Production{" +
                "cost=" + cost +
                ", earnings=" + earnings +
                '}';
    }
}
