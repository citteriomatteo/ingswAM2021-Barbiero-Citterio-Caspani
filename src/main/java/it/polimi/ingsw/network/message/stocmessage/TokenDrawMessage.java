package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies every player about the extracted token.
 * Message structure: { token name string }
 */

public class TokenDrawMessage extends StoCMessage {

    private final StoCMessageType type = StoCMessageType.TOKEN_DRAW;
    private final String tokenName;

    public TokenDrawMessage(String nickname, String tokenName){
        super(nickname);
        this.tokenName = tokenName;
    }

    public StoCMessageType getType(){ return type; }
    public String tokenName(){ return tokenName; }

}
