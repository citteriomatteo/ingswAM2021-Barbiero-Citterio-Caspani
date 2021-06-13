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
     * Set to print all about the match and has to be redefined in each View class.
     * @param match the match
     */
    void showAll(LightMatch match);
    /**
     * Set to print a list of requested cards and has to be redefined in each View class.
     * @param id the list of cards to print
     */
    void drawCards(List<String> id);


    /**
     * Set to print the game title and has to be redefined in each View class.
     */
    void printTitle();
    /**
     * Set to print the login layout and has to be redefined in each View class.
     */
    void drawLoginLayout();
    /**
     * Set to print the reconnection message layout and has to be redefined in each View class.
     */
    void drawReconnectionLayout();
    /**
     * Set to print the single/multi choice layout and has to be redefined in each View class.
     */
    void drawNewPlayerLayout();
    /**
     * Set to print the "number of players" choice layout and has to be redefined in each View class.
     */
    void drawNumPlayersLayout();
    /**
     * Set to print the configuration choice layout and has to be redefined in each View class.
     */
    void drawConfigurationChoice();
    /**
     * Set to print the configuration build layout and has to be redefined in each View class.
     */
    void drawConfigurationLayout();
    /**
     * Set to print the waiting layout and has to be redefined in each View class.
     */
    void drawWaitingLayout();
    /**
     * Set to print the "starting phase end" waiting layout and has to be redefined in each View class.
     */
    void drawStartingPhaseDone();
    /**
     * Set to print the leaders choice layout and has to be redefined in each View class.
     */
    void drawLeadersChoiceLayout();
    /**
     * Set to print the starting resources choice layout and has to be redefined in each View class.
     * @param yourPosition to print a more precise layout on how many resources you can pick
     */
    void drawResourcesChoiceLayout(int yourPosition);
    /**
     * Set to print the "your turn" or "wait for your turn" layout and has to be redefined in each View class.
     * @param yourTurn your turn/not your turn
     */
    void drawYourTurnLayout(boolean yourTurn);
    /**
     * Set to print the white marble conversions layout and has to be redefined in each View class.
     */
    void drawWhiteMarbleConversionsLayout();
    /**
     * Set to print the resources placement layout and has to be redefined in each View class.
     */
    void drawResPlacementLayout();
    /**
     * Set to print the dev card payments layout and has to be redefined in each View class.
     */
    void drawBuyDevCardLayout();
    /**
     * Set to print the dev card placement layout and has to be redefined in each View class.
     */
    void drawPlaceDevCardLayout();
    /**
     * Set to print the production layout and has to be redefined in each View class.
     */
    void drawProductionLayout();
    /**
     * Set to print the end of turn layout and has to be redefined in each View class.
     */
    void drawEndTurnLayout();
    /**
     * Set to print the end of match layout and has to be redefined in each View class.
     */
    void drawEndMatchLayout();
    /**
     * Set to print the "rematch offered" layout and has to be redefined in each View class.
     */
    void drawRematchOfferLayout(String nickname);
    /**
     * Set to print the goodbye layout in case of server disconnection and has to be redefined in each View class.
     */
    void drawGoodbyeLayout(String msg);

    /**
     * Set to print a new token's draw in single-player matches and has to be redefined in each View class.
     * @param tokenName the name of the token
     * @param remainingTokens the number of tokens left
     */
    void printTokenDraw(String tokenName, int remainingTokens);
    /**
     * Set to print the last round flag/notify and has to be redefined in each View class.
     */
    void printLastRound();
    /**
     * Set to print the final match ranking and has to be redefined in each View class.
     * @param message an end match message
     * @param ranking the non-sorted ranking map
     */
    void printMatchResults(String message, Map<String, Integer> ranking);
    /**
     * Set to notify the player about a bad choice he has done, and has to be redefined in each View class.
     * @param errMessage the error message
     * @param currentState the state after the unacceptable move (usually the same as before)
     * @param match the match
     */
    void printRetry(String errMessage, StateName currentState, LightMatch match);
    /**
     * Set to print the discount map in the CLI version of the game.
     * @param player the light player
     */
    void printDiscountMap(LightPlayer player);
    /**
     * Set to print the white marble conversions' map in the CLI version of the game.
     * @param player the light player
     */
    void printWhiteMarbleConversions(LightPlayer player);

    /**
     * Setter
     * @param lastRound the last round flag
     */
    void setLastRound(boolean lastRound);

}
