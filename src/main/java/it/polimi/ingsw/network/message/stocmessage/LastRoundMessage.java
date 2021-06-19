package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the LastRoundMessage from server to client, to notify everyone that the last round has started.
 * Message structure: { nickname }
 */
public class LastRoundMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.LAST_ROUND;

    /**
     * Constructor of a last round's message.
     * @param nickname the receiver
     */
    public LastRoundMessage(String nickname) {
        super(nickname);
    }

    @Override
    public boolean compute(Client client){
        client.getController().printLastRound();
        return true;
    }

    @Override
    public StoCMessageType getType() { return type; }
}
