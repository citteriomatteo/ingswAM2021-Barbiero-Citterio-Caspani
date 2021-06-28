package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements the BinarySelectionMessage.
 */
public class BinarySelectionMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.BINARY_SELECTION;
    private final boolean selection;
    private final String comment;

    /**
     * Constructor with comment
     * @param nickname the sender
     * @param selection the selection value
     * @param comment the comment
     */
    public BinarySelectionMessage(String nickname, boolean selection, String comment){
            super(nickname);
            this.selection = selection;
            this.comment = comment;
        }

    /**
     * Constructor without comment
     * @param nickname the sender
     * @param selection the selection value
     */
    public BinarySelectionMessage(String nickname, boolean selection) {
        super(nickname);
        this.selection = selection;
        comment = null;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        if(getIsLocal() || controlBase.getInitController().selection(selection))
            return true;
        controlBase.write(new RetryMessage(controlBase.getNickname(), controlBase.getCurrentState(), "You can't send a " + type + " message right now"));
        return false;
    }
    @Override
    public boolean isSomethingNull(){
        return getNickname() == null || comment == null;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }
    /**
     * Getter
     * @return the selection value
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
