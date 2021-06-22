package it.polimi.ingsw.gameLogic.model.essentials.leader;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.essentials.Resource;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.Effector;
import it.polimi.ingsw.view.CLI.ClientCLI;
import it.polimi.ingsw.view.CLI.ColorCli;

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
     * @param effector the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return true
     */
    @Override
    public boolean activate(Effector effector) {
        return true;
    }

    @Override
    public String toString() {
        return "ProductionEffect{" +
                "production=" + production +
                '}';
    }

    @Override
    public String toCLIString() {
        String symbol = ColorCli.RED + "¤ " + ColorCli.CLEAR;

        StringBuilder str = new StringBuilder();
        str.append(symbol).append("Production effect \nCosts:    ");
        for(PhysicalResource r : production.getCost()) {
            str.append("  [");
            ClientCLI.addColouredResource(r, str);
            str.append(", ").append(r.getQuantity()).append("]");
        }
        str.append("\nEarnings: ");
        for(Resource r : production.getEarnings()) {
            if (r.isPhysical()) {
                str.append("  [");
                ClientCLI.addColouredResource(r, str);
                str.append(", ").append(r.getQuantity()).append("]");
            }
            else
                str.append("[").append(ColorCli.RED).append(" ┼ ").append(ColorCli.CLEAR).append("]");
        }
        str.append("\n");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionEffect that = (ProductionEffect) o;
        return Objects.equals(getProduction(), that.getProduction());
    }

    /**
     * This method returns the effect type.
     * @return the symbol
     */
    @Override
    public String getEffectSymbol(){
        return ColorCli.RED + "¤" + ColorCli.CLEAR;
    }

}