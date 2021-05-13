package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

public class BinarySelectionMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.BINARY_SELECTION;
    private final boolean selection;
    private final String comment;

    public BinarySelectionMessage(String nickname, boolean selection, String comment){
            super(nickname);
            this.selection = selection;
            this.comment = comment;
        }

    public BinarySelectionMessage(String nickname, boolean selection) {
        super(nickname);
        this.selection = selection;
        comment = null;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        return controlBase.getInitController().selection(selection);
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

    public boolean getSelection() {
        return selection;
    }

    public String getComment() {
        return comment;
    }
}
