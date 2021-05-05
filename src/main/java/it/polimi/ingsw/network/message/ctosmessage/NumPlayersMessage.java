package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.stocmessage.RetryMessage;
import it.polimi.ingsw.network.server.ControlBase;

import static it.polimi.ingsw.network.server.ServerUtilities.searchingForPlayers;

public class NumPlayersMessage extends CtoSMessage{
    private static final CtoSMessageType type = CtoSMessageType.NUM_PLAYERS;
    private final int numPlayers;

    public NumPlayersMessage(String nickname, int numPlayers) {
        super(nickname);
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    private boolean isValid(){
        return numPlayers >= 2 && numPlayers <= 4;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        Player submitter = controlBase.getPlayer();
        if (isValid()) {
            searchingForPlayers(submitter, numPlayers);
            System.out.println(submitter + " is searching for " + numPlayers + " players...");
            return true;
        }
        controlBase.write(new RetryMessage(submitter.getNickname(), "Wrong number of players," +
                " you have to choose a number of players between 2 and 4 to organize a multiplayer match"));
        return false;
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }
}
