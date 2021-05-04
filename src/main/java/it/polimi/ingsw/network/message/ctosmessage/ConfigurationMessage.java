package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ControlBase;

/**
 * This class implements a message from the client to initialize the configuration of the match
 * Message structure: { nickname, configuration }
 */
public class ConfigurationMessage extends CtoSMessage {
    private static final CtoSMessageType type = CtoSMessageType.CONFIGURATION;
    private final MatchConfiguration config;

    public ConfigurationMessage(String nickname, MatchConfiguration config){
        super(nickname);
        this.config = config;
    }

    /**
     * Getter
     * @return the configuration of the match
     */
    public MatchConfiguration getConfig(){
        return config;
    }

    @Override
    public boolean computeMessage(ControlBase controlBase) {
        //TODO
        return false;
    }

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
