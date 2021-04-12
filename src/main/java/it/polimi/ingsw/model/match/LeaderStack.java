package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.leader.LeaderCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class LeaderStack {
    private final Stack<LeaderCard> deck;

    public LeaderStack(List<LeaderCard> cards) {
        Collections.shuffle(cards);
        deck = (new Stack<>());
        deck.addAll(cards);
    }

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
