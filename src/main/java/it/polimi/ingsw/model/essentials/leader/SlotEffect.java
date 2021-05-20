package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effector;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.cli.ColorCli;

import java.util.Objects;
/**
 * Auxiliary class used for implementing Strategy pattern for the LeaderCard effect: Extra Slot
 */
public class SlotEffect implements Effect{
    private final PhysicalResource extraShelf;

    /**
     * Simple constructor
     * @param extraShelf set in 'quantity' the size of the new shelf that contains only resource of the relative type
     */
    public SlotEffect(PhysicalResource extraShelf) {
        this.extraShelf = extraShelf;
    }

    /**
     * getter for testing
     * @return the PhysicalResource indicating the extra shelf
     */
    PhysicalResource getExtraShelf() {
        return extraShelf;
    }

    /**
     * Evolves the warehouse adding the extra slot
     * @param effector the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return false
     */
    @Override
    public boolean activate(Effector effector)
    {
        effector.warehouseEvolution(extraShelf);
        return false;
    }

    @Override
    public String toString() {
        return "SlotEffect{" +
                "extraShelf=" + extraShelf +
                '}';
    }

    @Override
    public String toCLIString() {
        String symbol = ColorCli.RED + "≡ " + ColorCli.CLEAR;

        StringBuilder str = new StringBuilder();
        str.append(symbol).append("Extra shelf: ");
        Cli.addColouredResource(extraShelf, str);
        str.append(", ").append(extraShelf.getQuantity());
        str.append("\n");

        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotEffect that = (SlotEffect) o;
        return Objects.equals(extraShelf, that.extraShelf);
    }

    /**
     * This method returns the effect type.
     * @return the symbol
     */
    @Override
    public String getEffectSymbol(){
        return ColorCli.RED + "≡" + ColorCli.CLEAR;
    }


}