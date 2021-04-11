package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;

public class SlotEffect implements Effect{
    // The 'extraShelf' variable set in 'quantity' the size of the new shelf that contains only resource of the relative type
    private final PhysicalResource extraShelf;

    public SlotEffect(PhysicalResource extraShelf) {
        this.extraShelf = extraShelf;
    }

    @Override
    public boolean activate(Effecter effecter)
    {
        try{ effecter.warehouseEvolution(extraShelf); }
        catch(NegativeQuantityException e) {e.printStackTrace();}
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