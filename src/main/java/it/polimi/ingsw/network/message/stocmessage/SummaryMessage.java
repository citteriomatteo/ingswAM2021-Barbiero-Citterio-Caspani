package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.PlayerSummary;
import it.polimi.ingsw.network.client.Client;

import java.util.stream.Collectors;

/**
 * This class implements the SummaryMessage from server to client.
 * It is sent to every player at the beginning of the match or after a reconnection.
 * Message structure: { nickname, already compiled summary object }
 */
public class SummaryMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.SUMMARY;
    private final Summary summary;

    /**
     * Constructor of the message containing the current summary.
     * @param nickname the nickname
     * @param summary the summary
     */
    public SummaryMessage(String nickname, Summary summary) {
        super(nickname);

        for(PlayerSummary ps : summary.getPlayersSummary())
            if(!ps.getNickname().equals(nickname))
                ps.getHandLeaders().stream().map((x)->(LeaderCard) null).collect(Collectors.toList());  //todo: why here?

        this.summary = summary;
    }

    @Override
    public boolean compute(Client client){
        client.getController().setLightMatch(summary);
        return true;
    }

    @Override
    public StoCMessageType getType() { return type; }
    /**
     * Getter
     * @return the summary
     */
    public Summary getSummary() { return summary; }
}
