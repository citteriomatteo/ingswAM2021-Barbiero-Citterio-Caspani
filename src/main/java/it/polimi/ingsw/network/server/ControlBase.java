package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.InitController;
import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;

public interface ControlBase {
    StateName getCurrentState();
    MatchController getMatchController();
    InitController getInitController();
    void setPlayer(Player player);
    Player getPlayer();
    String getNickname();
    void setMatchController(MatchController controller);

    /**
     * Writes something at this player, the message has to be written in json in the correct format
     * @param msg the message you want to send to this player
     * @return true if the message has been sent, false if something goes wrong in the output stream
     */
    boolean write(StoCMessage msg);
}
