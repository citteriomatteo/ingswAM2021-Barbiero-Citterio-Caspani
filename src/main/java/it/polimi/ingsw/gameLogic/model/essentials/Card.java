package it.polimi.ingsw.gameLogic.model.essentials;

/**
 * This interface unifies Development and Leader cards.
 */
public interface Card {
    /**
     * Shows if this card is a Leader one
     * @return true if it's a leader, false elsewhere
     */
    boolean isLeader();
}
