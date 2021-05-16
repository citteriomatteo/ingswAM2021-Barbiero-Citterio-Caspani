package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.network.client.Client;

public class SummaryMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.SUMMARY;
    private final Summary summary;

    public SummaryMessage(String nickname, Summary summary) {
        super(nickname);
        this.summary = summary;
    }

    @Override
    public boolean compute(Client client){
        client.getController().setLightMatch(summary);
        return true;
    }

    @Override
    public StoCMessageType getType() { return type; }
    public Summary getSummary() { return summary; }
}
