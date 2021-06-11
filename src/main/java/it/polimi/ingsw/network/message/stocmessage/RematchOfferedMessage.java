package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements the RematchOfferedMessage from server to client.
 * Message structure: { nickname, rematch proposer }
 */
public class RematchOfferedMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.REMATCH_OFFERED;
    private final String proposer;

    public RematchOfferedMessage(String nickname, String proposer) {
        super(nickname);
        this.proposer = proposer;
    }

    @Override
    public boolean compute(Client client){
        client.getController().printRematchOffer(this.proposer);
        return true;
    }

    @Override
    public StoCMessageType getType() {
        return type;
    }

    public String getMessage() {
        return proposer;
    }
}
