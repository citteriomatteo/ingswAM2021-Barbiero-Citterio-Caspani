package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message that notifies every player about the extracted token.
 * Message structure: { token name string }
 */

public class TokenDrawMessage extends Message {

    private final StoCMessageType type = StoCMessageType.TOKEN_DRAW;
    private String tokenName;

    public TokenDrawMessage(String nickname, String tokenName){
        super(nickname);
        this.tokenName = tokenName;
    }

    private StoCMessageType getType(){ return type; }
    private String tokenName(){ return tokenName; }

}
