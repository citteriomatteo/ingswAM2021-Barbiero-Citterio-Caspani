package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

import java.util.List;

import static it.polimi.ingsw.view.ClientController.getClientController;

/**
 * This class implements the BinarySelectionMessage from server to client.
 * Message structure: { nickname, selection, string-made comment }
 */
public class HandLeadersStateMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.HAND_LEADERS_STATE;
    private final List<String> handLeaders;

    /**
     * Constructor of an hand leaders' state message.
     * @param nickname the receiver
     * @param handLeaders the new state
     */
    public HandLeadersStateMessage(String nickname, List<String> handLeaders) {
        super(nickname);
        this.handLeaders = handLeaders;
    }

    @Override
    public boolean compute(Client client){
        getClientController().getMatch().setHandLeaders(getNickname(), handLeaders);
        return true;
    }
    @Override
    public StoCMessageType getType() { return type; }

    /**
     * Getter
     * @return the new hand leaders' version
     */
    public List<String> getHandLeaders() { return handLeaders; }

}
