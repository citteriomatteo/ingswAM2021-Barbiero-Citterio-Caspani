package it.polimi.ingsw.model.match.token;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.SingleMatch;

import java.util.Collections;
import java.util.Stack;

public class TokenStack {
    private Stack<Token> tokenStack = new Stack<>();

    public TokenStack() {

        tokenStack.push(new BlueToken());
        tokenStack.push(new GreenToken());
        tokenStack.push(new PurpleToken());
        tokenStack.push(new YellowToken());
        tokenStack.push(new PlusOneShuffleToken());
        tokenStack.push(new PlusTwoToken());
        tokenStack.push(new PlusTwoToken());

        Collections.shuffle(tokenStack);
    }

    public Stack<Token> getStack() {
        return (Stack<Token>) tokenStack.clone();
    }

    public boolean draw(SingleMatch match) throws MatchEndedException {

        return tokenStack.pop().onDraw(match);
    }

    public boolean shuffle(){
        tokenStack = new Stack<>();

        tokenStack.push(new BlueToken());
        tokenStack.push(new GreenToken());
        tokenStack.push(new PurpleToken());
        tokenStack.push(new YellowToken());
        tokenStack.push(new PlusOneShuffleToken());
        tokenStack.push(new PlusTwoToken());
        tokenStack.push(new PlusTwoToken());

        Collections.shuffle(tokenStack);

        return true;
    }


}
