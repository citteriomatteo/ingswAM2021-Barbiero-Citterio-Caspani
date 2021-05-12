package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.Summary;

public class SummaryMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.SUMMARY;
    private final Summary summary;

    public SummaryMessage(String nickname, Summary summary) {
        super(nickname);
        this.summary = summary;
    }

    @Override
    public StoCMessageType getType() { return type; }
    public Summary getSummary() { return summary; }
}
