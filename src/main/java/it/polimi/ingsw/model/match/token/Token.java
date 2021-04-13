package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.SingleMatch;

public abstract class Token {
    public abstract boolean onDraw(SingleMatch match) throws MatchEndedException, NegativeQuantityException;
}
