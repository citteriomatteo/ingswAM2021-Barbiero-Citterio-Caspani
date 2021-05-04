package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.InitController;
import it.polimi.ingsw.controller.MatchController;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;

public interface ControlBase {
    MatchController getMatchController();
    InitController getInitController();
    boolean write(StoCMessage msg);
}
