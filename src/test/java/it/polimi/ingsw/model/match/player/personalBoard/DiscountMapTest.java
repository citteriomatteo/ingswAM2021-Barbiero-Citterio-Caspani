package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscountMapTest
{
    private final DiscountMap discountMap = new DiscountMap();

    @Test
    public void testSetDiscount() throws NegativeQuantityException
    {
        PhysicalResource resource = new PhysicalResource(ResType.COIN, 1);
        this.discountMap.setDiscount(resource);
        Assertions.assertFalse(this.discountMap.getDiscountMap().isEmpty());
        Assertions.assertEquals(1, this.discountMap.getDiscountMap().get(ResType.COIN));
        Assertions.assertEquals(0, this.discountMap.getDiscountMap().get(ResType.SERVANT));
        this.discountMap.setDiscount(resource);
        Assertions.assertTrue(this.discountMap.getDiscountMap().containsKey(ResType.COIN) && this.discountMap.getDiscountMap().get(ResType.COIN) == 2);
    }

    @Test
    public void testApplyDiscount() throws NegativeQuantityException
    {
        PhysicalResource resource = new PhysicalResource(ResType.COIN, 2);
        PhysicalResource resource1 = new PhysicalResource(ResType.COIN, 3);
        PhysicalResource resource2 = new PhysicalResource(ResType.STONE, 1);
        PhysicalResource resource3 = new PhysicalResource(ResType.STONE, 2);
        this.discountMap.setDiscount(resource);
        Assertions.assertTrue(resource2.equals(this.discountMap.applyDiscount(resource2)) && resource2.getQuantity() == this.discountMap.applyDiscount(resource2).getQuantity());
        Assertions.assertTrue(resource1.equals(this.discountMap.applyDiscount(resource1)) && resource1.getQuantity() - this.discountMap.getDiscountMap().get(resource1.getType()) == this.discountMap.applyDiscount(resource1).getQuantity());
        this.discountMap.setDiscount(resource3);
        Assertions.assertTrue(resource2.equals(this.discountMap.applyDiscount(resource2)) && this.discountMap.applyDiscount(resource2).getQuantity() == 0);
    }
}
