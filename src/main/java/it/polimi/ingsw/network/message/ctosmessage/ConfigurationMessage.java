package it.polimi.ingsw.network.message.ctosmessage;

import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.network.message.Message;

/**
 * This class implements a message from the client to initialize the configuration of the match
 * Message structure: { nickname, configuration }
 */

public class ConfigurationMessage extends Message {
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

    /**
     * Getter
     * @return the message's type
     */
    public CtoSMessageType getType() {
        return type;
    }

}
