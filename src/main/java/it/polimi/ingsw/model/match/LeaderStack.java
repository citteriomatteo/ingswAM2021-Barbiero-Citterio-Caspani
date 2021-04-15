package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.leader.LeaderCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * A deck of LeaderCards
 * @see LeaderCard
 */
public class LeaderStack {
    private final Stack<LeaderCard> deck;

    /**
     * Constructor, it takes all the playable LeaderCards of the match, shuffle them and then and form the deck
     * @param cards a list of all the playable LeaderCards of the match
     */
    public LeaderStack(List<LeaderCard> cards) {
        Collections.shuffle(cards);
        deck = new Stack<>();
        deck.addAll(cards);
    }

    /**
     * Draws the first 'numLeaders' cards from the top of the deck
     * @param numLeaders number of cards to draw
     * @return the list of the drawn cards
     */
    public List<LeaderCard> draw(int numLeaders){
        List<LeaderCard> res = new ArrayList<>();

        if (numLeaders>deck.size()) {
            for (int i = 0; i < deck.size(); i++) {
                res.add(deck.pop());
            }
        }

        else {
            for (int i = 0; i < numLeaders; i++) {
                res.add(deck.pop());
            }
        }
        return res;
    }
}
