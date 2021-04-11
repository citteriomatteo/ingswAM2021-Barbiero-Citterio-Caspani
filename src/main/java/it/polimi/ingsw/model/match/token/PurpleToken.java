package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;

public class PurpleToken extends Token{
    @Override
    public boolean onDraw(SingleMatch match) throws MatchEndedException {
        return match.getSingleCardGrid().discard(CardColor.PURPLE);
    }
}
