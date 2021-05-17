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

    public TokenDrawMessage(String nickname, String tokenName, int remainingTokens){
        super(nickname);
        this.tokenName = tokenName;
        this.remainingTokens = remainingTokens;
    }

    public StoCMessageType getType(){ return type; }
    public int getRemainingTokens(){ return remainingTokens; }
    public String tokenName(){ return tokenName; }

    @Override
    public boolean compute(Client client){
        client.getController().printTokenDraw(tokenName, remainingTokens);
        return true;
    }

}
