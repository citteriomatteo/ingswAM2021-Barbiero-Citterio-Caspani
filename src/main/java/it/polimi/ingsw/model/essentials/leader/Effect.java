package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

public interface Effect {

    //This method is called by the leader when it is activated ->
    // it set the relative effect in the playerBoard, then return true if the effect has a production
    boolean activate(Effecter effecter);
}
