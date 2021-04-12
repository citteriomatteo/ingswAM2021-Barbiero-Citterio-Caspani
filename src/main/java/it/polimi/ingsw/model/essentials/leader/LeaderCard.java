package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.Requirable;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Verificator;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.List;
import java.util.Objects;

/**
 * Class that represent a Leader card of the game
 */
public class LeaderCard {
    private final List<Requirable> requirements;
    private final int winPoints;
    private final Effect effect;

    /**
     * Getter
     * @return the list of requirements needed to be satisfied in order to place the card
     */
    public List<Requirable> getRequirements() {
        return requirements;
    }

    /**
     * Getter
     * @return the amount of win points the card give
     */
    public int getWinPoints() {
        return winPoints;
    }

    /**
     * Getter
     * @return the effect of the card
     */
    public Effect getEffect() {
        return effect;
    }


    /**
     * Simple constructor
     * @param requirements the list of requirements needed to be satisfied in order to place the card
     * @param winPoints the amount of win points the card give
     * @param effect the effect of the card
     */
    public LeaderCard(List<Requirable> requirements, int winPoints, Effect effect) {
        this.requirements = requirements;
        this.winPoints = winPoints;
        this.effect = effect;
    }

    // Verify if the player has satisfied all the requirements

    /**
     * Verifies if the player has satisfied all the requirements
     * @param verificator the player who wants to activate the Leader
     * @return true if the requirements are satisfied
     */
    public boolean isActivable(Verificator verificator){
        for (Requirable req : requirements){
            if(!req.verify(verificator))
                return false;
        }
        return true;
    }

    // Activate the precise effect and then add the leader to the ActiveLeader List
    // The player must have already called isActivable himself before

    /**
     * Activate the precise effect and then add the leader to the ActiveLeader List
     * @requires The player must have already called isActivable himself before.
     * @param effecter the PersonalBoard owned by the player who wants to activate the LeaderCard
     * @return true if the method has worked correctly
     */
    public boolean activate (Effecter effecter) throws NegativeQuantityException {  //TODO remove this throw of exception
        return effecter.addActiveLeader(this, effect.activate(effecter));
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "\nrequirements=" + requirements +
                ",\nwinPoints=" + winPoints +
                ",\neffect=" + effect +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderCard that = (LeaderCard) o;
        return getWinPoints() == that.getWinPoints() && Objects.equals(getRequirements(), that.getRequirements()) && Objects.equals(getEffect(), that.getEffect());
    }

}
