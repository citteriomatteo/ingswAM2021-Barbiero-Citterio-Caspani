package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;

public class ProductionEffect implements Effect{
    private final Production production;

    public ProductionEffect(Production production) {
        this.production = production;
    }
    public Production getProduction() {
        return production;
    }


    // Return true without doing anything
    // because the concrete effect is realized moving the leader in the productionActiveLeader list
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