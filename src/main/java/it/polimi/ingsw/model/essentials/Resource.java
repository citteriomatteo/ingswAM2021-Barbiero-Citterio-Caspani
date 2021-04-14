package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.player.Adder;

public interface Resource {

    //Adds the resource to the StrongBox or increments the faitMarker
    boolean add(Adder adder) throws MatchEndedException;
}
