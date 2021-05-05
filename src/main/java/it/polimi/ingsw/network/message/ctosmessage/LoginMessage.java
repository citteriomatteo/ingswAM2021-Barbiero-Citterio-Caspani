package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.server.ControlBase;

public class LoginMessage extends CtoSMessage{
    private static final CtoSMessageType type = CtoSMessageType.LOGIN;

    public LoginMessage(String nickname) {
        super(nickname);
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        //TODO: check if exist a player with the same name
        controlBase.setPlayer(new Player(getNickname()));
        return true;
    }

    @Override
    public CtoSMessageType getType() {
        return type;
    }
}
