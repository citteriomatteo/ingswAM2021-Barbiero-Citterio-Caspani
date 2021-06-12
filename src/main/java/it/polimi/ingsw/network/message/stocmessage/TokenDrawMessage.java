package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements a message that notifies every player about the extracted token.
 * Message structure: { token name string }
 */
public class TokenDrawMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.TOKEN_DRAW;
    private final String tokenName;
    private final int remainingTokens;

    /**
     * Constructor of the token draw message.
     * @param nickname the nickname
     * @param tokenName the token string name
     * @param remainingTokens the number of remaining tokens on the stack
     */
    public TokenDrawMessage(String nickname, String tokenName, int remainingTokens){
        super(nickname);
        this.tokenName = tokenName;
        this.remainingTokens = remainingTokens;
    }

    @Override
    public StoCMessageType getType(){ return type; }
    /**
     * Getter
     * @return the remaining tokens number
     */
    public int getRemainingTokens(){ return remainingTokens; }
    /**
     * Getter
     * @return the token name
     */
    public String getTokenName(){ return tokenName; }

    @Override
    public boolean compute(Client client){
        client.getController().printTokenDraw(tokenName, remainingTokens);
        return true;
    }

}
