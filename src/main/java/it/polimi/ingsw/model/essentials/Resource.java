package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.match.player.Adder;

public interface Resource {

    //This metod add the resource to the StrongBox or increse the faitMarker

    boolean add(Adder adder) throws InvalidAddFaithException;
}
