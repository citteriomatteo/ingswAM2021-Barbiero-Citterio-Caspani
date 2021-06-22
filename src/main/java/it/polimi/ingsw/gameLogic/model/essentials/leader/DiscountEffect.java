package it.polimi.ingsw.gameLogic.model.essentials.leader;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.Effector;
import it.polimi.ingsw.view.CLI.ClientCLI;
import it.polimi.ingsw.view.CLI.ColorCli;

import java.util.Objects;
/**
 * Auxiliary class used for implementing Strategy pattern for the LeaderCard effect : Discount
 */
public class DiscountEffect implements Effect{
    private final PhysicalResource discount;

    /**
     * Simple constructor
     * @param discount set in 'quantity' the entity of the discount on a resource of the relative type
     */
    public DiscountEffect(PhysicalResource discount) {
        this.discount = discount;
    }

    /**
     * getter for testing
     * @return the discount given by the effect
     */
    PhysicalResource getDiscount() {
        return discount;
    }

    /**
     * Sets the relative discount in the discount map
     * @param effector the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return false
     */
    @Override
    public boolean activate(Effector effector)
    {
        effector.setDiscount(discount);
        return false;
    }

    @Override
    public String toString() {
        return "DiscountEffect{" +
                "discount=" + discount +
                '}';
    }

    @Override
    public String toCLIString() {
        String symbol = ColorCli.GREEN.paint("½ ");

        StringBuilder res = new StringBuilder();
        res.append(symbol).append("Discount effect: ");
        ClientCLI.addColouredResource(this.discount, res);
        res.append(", ").append(this.discount.getQuantity());
        res.append("\n");

        return res.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountEffect that = (DiscountEffect) o;
        return Objects.equals(discount, that.discount);
    }

    /**
     * This method returns the effect type.
     * @return the symbol
     */
    @Override
    public String getEffectSymbol(){
        return ColorCli.GREEN.paint("½");
    }


}
