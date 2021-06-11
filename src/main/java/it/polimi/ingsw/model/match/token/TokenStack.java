package it.polimi.ingsw.model.match.token;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.model.match.SingleMatch;

import java.util.Collections;
import java.util.Stack;

/**
 * This class handles correctly the token stack for single player's Lorenzo extractions.
 */
public class TokenStack {
    private Stack<Token> tokenStack = new Stack<>();

    /**
     * Constructor. Create a stack with one blueToken, one greenToken, one PurpleToken, one yellowToken,
     * one plusOneShuffleToken, two plusTwoToken and it shuffles the stack.
     */
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

    /**
     * Getter
     * @return a copy of the stack
     */
    public Stack<Token> getStack() {
        return (Stack<Token>) tokenStack.clone();
    }

    /**
     * This method draw a single token and activate its effect
     * @param match the actual singleMatch
     * @return true if the method works
     * @throws LastRoundException if the number of a certain type of card in singleCardGrid became 0
     */
    public boolean draw(SingleMatch match) throws LastRoundException {

        return tokenStack.pop().onDraw(match);
    }

    /**
     * This method recreate a new shuffled tokenStack
     * @return true
     */
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
