package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import java.util.HashMap;
import java.util.Map;

public class DiscountMap
{
    private final Map<ResType, Integer> discountMap;

    /**
     * Constructor
     * create a map where the ResType are the keys
     */
    public DiscountMap()
    {
        discountMap = new HashMap<>();
        for(ResType type: ResType.values())
            discountMap.put(type, 0);
    }

    /** @return the map of discounts */
    public Map<ResType, Integer> getDiscountMap() {
        return this.discountMap;
    }

    /**
     * This method adds the quantity of resource to the value mapped by resource type
     * @param resource the parameter resource contains the type of the key and the quantity of discount to add
     * @return true
     */
    public boolean setDiscount(PhysicalResource resource)
    {
        discountMap.replace(resource.getType(), (discountMap.get(resource.getType()) + resource.getQuantity()));
        return true;
    }

    /**
     * This method receive a PhysicalResource and returns the same resource if there isn't a discount for that resource,
     * or returns a new PhysicalResource with quantity reduced by the discount
     * @param resource a PhysicalResource to be discounted
     * @return a new discounted PhysicalResource
     */
    public PhysicalResource applyDiscount(PhysicalResource resource)
    {
        Integer discount = this.discountMap.get(resource.getType());
        if (discount == null)
            return resource;
        else {
            if (discount > resource.getQuantity())
                discount = resource.getQuantity();
            try {
                return new PhysicalResource(resource.getType(), resource.getQuantity() - discount);
            } catch (NegativeQuantityException e) {
                e.printStackTrace(); System.err.println("Application shutdown due to an internal error in "+this.getClass().getSimpleName()+".");
                System.exit(1);
            }
        }
        return null;
    }
}
