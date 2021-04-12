package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;
/**
 * Auxiliary class used for implementing Strategy pattern for the LeaderCard effect: White Marble Conversion
 */
public class WhiteMarbleEffect implements Effect{
    private final PhysicalResource conversion;

    /**
     * Simple constructor
     * @param conversion set in 'quantity' how many resources of the given type you can produce from a white marble draw
     */
    public WhiteMarbleEffect(PhysicalResource conversion) {
        this.conversion = conversion;
    }

    /**
     * Adds a new possible conversion for the white marble
     * @param effecter the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return false
     */
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