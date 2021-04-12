package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;
/**
 * Auxiliary class used for implementing Strategy pattern for the LeaderCard effect: Production
 */
public class ProductionEffect implements Effect{
    private final Production production;

    /**
     * Simple constructor
     * @param production the production of the leader
     */
    public ProductionEffect(Production production) {
        this.production = production;
    }

    /**
     * Getter
     * @return the production
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Return true without doing anything
     * because the concrete effect is realized moving the leader in the productionActiveLeader list
     * @param effecter the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return true
     */
    @Override
    public boolean activate(Effecter effecter) {
        return true;
    }

    @Override
    public String toString() {
        return "ProductionEffect{" +
                "production=" + production +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionEffect that = (ProductionEffect) o;
        return Objects.equals(getProduction(), that.getProduction());
    }

}