package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements the BinarySelectionMessage from server to client.
 * Message structure: { nickname, selection, string-made comment }
 */
public class BinarySelectionMessage extends StoCMessage {
    private static final StoCMessageType type = StoCMessageType.BINARY_SELECTION;
    private final boolean selection;
    private final String comment;

    /**
     * Constructor with comment.
     * @param nickname the receiver
     * @param selection the selection
     * @param comment the comment
     */
    public BinarySelectionMessage(String nickname, boolean selection, String comment){
        super(nickname);
        this.selection = selection;
        this.comment = comment;
    }

    /**
     * Constructor without comment.
     * @param nickname the receiver
     * @param selection the selection
     */
    public BinarySelectionMessage(String nickname, boolean selection) {
        super(nickname);
        this.selection = selection;
        comment = null;
    }

    @Override
    public boolean compute(Client client) {
        return true;
    }

    @Override
    public StoCMessageType getType() {
        return type;
    }

    /**
     * Getter
     * @return the server's selection
     */
    public boolean getSelection() {
        return selection;
    }
    /**
     * Getter
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

}