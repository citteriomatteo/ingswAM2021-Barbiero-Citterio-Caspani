package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import java.util.HashMap;
import java.util.Map;

public class DiscountMap {
    private final Map<ResType, Integer> discountMap;

    public DiscountMap()
    {
        discountMap = new HashMap<>();
    }

    public Map<ResType, Integer> getDiscountMap() {
        return this.discountMap;
    }

    public boolean setDiscount(PhysicalResource resource) {
        this.discountMap.put(resource.getType(), resource.getQuantity());
        return true;
    }

    public PhysicalResource applyDiscount(PhysicalResource resource) throws NegativeQuantityException {
        Integer discount = this.discountMap.get(resource.getType());
        if (discount == null) {
            return resource;
        } else {
            if (discount > resource.getQuantity()) {
                discount = resource.getQuantity();
            }

            return new PhysicalResource(resource.getType(), resource.getQuantity() - discount);
        }
    }
}
