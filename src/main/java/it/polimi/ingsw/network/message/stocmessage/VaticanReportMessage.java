package it.polimi.ingsw.network.message.stocmessage;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

/**
 * This class implements a message that contains the new state of a certain tile and implies a VIEW change of every player
 * on every client on the match.
 * It is possible to build the message one step at a time or all together.
 * Message structure: { nickname, number of the tile, Map of [player_nickname,new value of tile] }
 */
public class VaticanReportMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.VATICAN_REPORT;
    private final Map<String, List<Integer>> popeTiles;

    /**
     * Constructor of the vatican report message.
     * @param nickname the player interested in the pope tiles' change
     * @param popeTiles the latest pope tiles' situation
     */
    public VaticanReportMessage(String nickname, Map<String, List<Integer>> popeTiles){
        super(nickname);
        this.popeTiles = popeTiles;
    }

   @Override
    public StoCMessageType getType(){ return type; }
    /**
     * Getter
     * @return pope tiles' state
     */
    public Map<String, List<Integer>> getTilesState(){ return popeTiles; }

    @Override
    public boolean compute(Client client){
        //updates every player's tiles
        for(LightPlayer lp : client.getController().getMatch().getLightPlayers())
            client.getController().getMatch().setPopeTiles(lp.getNickname(), popeTiles.get(lp.getNickname()));
        return true;
    }

}
