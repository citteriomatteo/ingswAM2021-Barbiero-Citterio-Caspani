package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import it.polimi.ingsw.view.observer.ViewObserver;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all the common methods that every view must implement in its own way.
 * - for Cli: simple printing layouts
 * - for Gui: different javafx graphic modifies
 */
public interface View extends ViewObserver {

    /**
     * Shows all about the match.
     * Implementation may change consistently depending on the type of View used.
     * @param match the match
     */
    void showAll(LightMatch match);
    /**
     * Shows the list of requested cards.
     * Implementation may change consistently depending on the type of View used.
     * @param id the list of cards to print
     */
    void drawCards(List<String> id);

    /**
     * Shows the game title.
     * Implementation may change consistently depending on the type of View used.
     */
    void printTitle();
    /**
     * Shows the login layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawLoginLayout();
    /**
     * Shows the reconnection message layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawReconnectionLayout();
    /**
     * Shows the single/multi choice layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawNewPlayerLayout();
    /**
     * Shows the "number of players" choice layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawNumPlayersLayout();
    /**
     * Shows the configuration choice layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawConfigurationChoice();
    /**
     * Shows the configuration build layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawConfigurationLayout();
    /**
     * Shows the waiting layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawWaitingLayout();
    /**
     * Shows the "starting phase end" waiting layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawStartingPhaseDone();
    /**
     * Shows the leaders choice layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawLeadersChoiceLayout();
    /**
     * Shows the starting resources choice layout.
     * Implementation may change consistently depending on the type of View used.
     * @param yourPosition to print a more precise layout on how many resources you can pick
     */
    void drawResourcesChoiceLayout(int yourPosition);
    /**
     * Shows the "your turn" or "wait for your turn" layout.
     * Implementation may change consistently depending on the type of View used.
     * @param yourTurn your turn/not your turn
     */
    void drawYourTurnLayout(boolean yourTurn);
    /**
     * Shows the white marble conversions layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawWhiteMarbleConversionsLayout();
    /**
     * Shows the resources placement layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawResPlacementLayout();
    /**
     * Shows the dev card payments layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawBuyDevCardLayout();
    /**
     * Shows the dev card placement layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawPlaceDevCardLayout();
    /**
     * Shows the production layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawProductionLayout();
    /**
     * Shows the end of turn layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawEndTurnLayout();
    /**
     * Shows the end of match layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawEndMatchLayout();
    /**
     * Shows the "rematch offered" layout.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawRematchOfferLayout(String nickname);
    /**
     * Shows the goodbye layout in case of server disconnection.
     * Implementation may change consistently depending on the type of View used.
     */
    void drawGoodbyeLayout(String msg);

    /**
     * Shows a new token's draw in single-player matches.
     * Implementation may change consistently depending on the type of View used.
     * @param tokenName the name of the token
     * @param remainingTokens the number of tokens left
     */
    void printTokenDraw(String tokenName, int remainingTokens);
    /**
     * Graphically shows the last round.
     * Implementation may change consistently depending on the type of View used.
     */
    void printLastRound();
    /**
     * Shows the final match ranking.
     * Implementation may change consistently depending on the type of View used.
     * @param message an end match message
     * @param ranking the non-sorted ranking map
     */
    void printMatchResults(String message, Map<String, Integer> ranking);
    /**
     * Graphically notifies the player about a bad choice he has done.
     * Implementation may change consistently depending on the type of View used.
     * @param errMessage the error message
     * @param currentState the state after the unacceptable move (usually the same as before)
     * @param match the match
     */
    void printRetry(String errMessage, StateName currentState, LightMatch match);
    /**
     * Prints the discount map in the CLI version of the game.
     * @param player the light player
     */
    void printDiscountMap(LightPlayer player);
    /**
     * Prints the white marble conversions' map in the CLI version of the game.
     * @param player the light player
     */
    void printWhiteMarbleConversions(LightPlayer player);

    /**
     * Setter
     * @param lastRound the last round flag
     */
    void setLastRound(boolean lastRound);

}
