package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the client to switch two shelves
 * Message structure: { nickname, the number of the first shelf, the number of the second shelf }
 */
public class SwitchShelfMessage extends Message {
    private static final CtoSMessageType type = CtoSMessageType.SWITCH_SHELF;
    private final int shelf1;
    private final int shelf2;

    public SwitchShelfMessage(String nickname, int shelf1, int shelf2) {
        super(nickname);
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
    }

    /**
     * Getter
     * @return the first shelf
     */
    public int getShelf1() {
        return shelf1;
    }

    /**
     * Getter
     * @return the second shelf
     */
    public int getShelf2() {
        return shelf2;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SwitchShelfMessage{" +
                "shelf1=" + shelf1 +
                ", shelf2=" + shelf2 +
                '}';
    }
}
