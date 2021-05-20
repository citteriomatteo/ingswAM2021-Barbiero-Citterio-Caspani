package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.match.player.personalBoard.Effector;

/**
 * Interface used for implementing Strategy Pattern in LeaderCard
 */
public interface Effect {
    /**
     * This method is called by the leader when it is activated ->
     * It set the relative effect in the playerBoard, then return true if the effect has a production
     * @param effector the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return true if the LeaderCard have to be added to ProductionActiveLeader list after the activation
     */
    boolean activate(Effector effector);

    /**
     * This method returns the CLI-printable graphic of the leader's effect.
     * @return string
     */
    String toCLIString();

    /**
     * This method returns the effect type.
     */
    String getEffectSymbol();
}
