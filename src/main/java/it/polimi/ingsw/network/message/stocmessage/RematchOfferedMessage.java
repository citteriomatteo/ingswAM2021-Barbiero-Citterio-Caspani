package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements the RematchOfferedMessage from server to client.
 * It is sent when the server receives the first RematchMessage from one player in the match.
 * Message structure: { nickname, rematch proposer }
 */
public class RematchOfferedMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.REMATCH_OFFERED;
    private final String proposer;

    /**
     * Constructor of a rematch offered message.
     * @param nickname the receiver
     * @param proposer the nickname of the proposer
     */
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

    /**
     * Getter
     * @return the proposer's nickname
     */
    public String getMessage() {
        return proposer;
    }
}
