package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;

public class WhiteMarbleEffect implements Effect{
    //The 'conversion' variable set in 'quantity' how much resources of the given type you can produce from a white marble draw
    private final PhysicalResource conversion;

    public WhiteMarbleEffect(PhysicalResource conversion) {
        this.conversion = conversion;
    }

    @Override
    public boolean activate(Effecter effecter) {
        effecter.addNewConversion(conversion);
        return false;
    }

    @Override
    public String toString() {
        return "WhiteMarbleEffect{" +
                "conversion=" + conversion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhiteMarbleEffect that = (WhiteMarbleEffect) o;
        return Objects.equals(conversion, that.conversion);
    }

}