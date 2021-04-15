package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

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
     * @param effecter the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return false
     */
    @Override
    public boolean activate(Effecter effecter)
    {
        effecter.warehouseEvolution(extraShelf);
        return false;
    }

    @Override
    public String toString() {
        return "SlotEffect{" +
                "extraShelf=" + extraShelf +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotEffect that = (SlotEffect) o;
        return Objects.equals(extraShelf, that.extraShelf);
    }

}