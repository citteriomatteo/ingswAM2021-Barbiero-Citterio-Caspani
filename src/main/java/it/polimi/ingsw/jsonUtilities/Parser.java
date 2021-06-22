package it.polimi.ingsw.jsonUtilities;

import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

/**
 * Interface used for parsing of messages and reading from files, in order to separate
 * the language used from the network and model implementation
 */
public interface Parser {

    /**
     * Parses the message into the equivalent language used for communication purposes
     * @param message the message you want to translate
     * @return the equivalent string
     */
    String parseFromCtoSMessage(CtoSMessage message);

    /**
     * Parses the language used for communication purposes into the equivalent message
     * @param message the string you want to translate
     * @return the equivalent message
     */
    CtoSMessage parseInCtoSMessage(String message);

    /**
     * Parses the message into the equivalent language used for communication purposes
     * @param message the message you want to translate
     * @return the equivalent string
     */
    String parseFromStoCMessage(StoCMessage message);

    /**
     * Parses the language used for communication purposes into the equivalent message
     * @param message the string you want to translate
     * @return the equivalent message
     */
    StoCMessage parseInStoCMessage(String message);

    /**
     * Reads a matchConfiguration from a reader in a human-readable format and parses it to the relative java object
     * @param reader the reader where you want to read the formatted configuration from
     * @return the equivalent matchConfiguration object
     */
    MatchConfiguration readMatchConfiguration(@NotNull Reader reader);


}
