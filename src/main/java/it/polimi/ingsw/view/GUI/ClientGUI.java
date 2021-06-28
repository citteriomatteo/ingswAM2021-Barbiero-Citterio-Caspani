package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

/**
 * This class implements visual functions needed by the GUI version of the game to run.
 * It utilize JavaFX and fxml files.
 */
public class ClientGUI implements View {

    /**
     * Does nothing in GUI.
     * @param match the match
     */
    @Override
    public void showAll(LightMatch match) { }

    /**
     * Does nothing in GUI.
     * @param id the list of cards to print
     */
    @Override
    public void drawCards(List<String> id) { }

    /**
     * Does nothing in GUI.
     */
    @Override
    public void printTitle() { }

    /**
     * Calls a scene load on the scene proxy for the login phase.
     */
    @Override
    public void drawLoginLayout() {
        getSceneProxy().changeScene(SceneName.LoginScene);
    }

    /**
     * Calls a scene load on the scene proxy for the reconnection phase.
     */
    @Override
    public void drawReconnectionLayout() {
        getSceneProxy().changeScene(SceneName.ReconnectionScene);
    }

    /**
     * Calls a scene load on the scene proxy for the mode selection phase.
     */
    @Override
    public void drawNewPlayerLayout() {
        getSceneProxy().changeScene(SceneName.ModeSelectionScene);
    }

    /**
     * Calls a scene load on the scene proxy for the number of players choice phase.
     */
    @Override
    public void drawNumPlayersLayout() {
        getSceneProxy().changeScene(SceneName.NumPlayerScene);
    }

    /**
     * Calls a scene load on the scene proxy for the configuration choice phase.
     */
    @Override
    public void drawConfigurationChoice() {
        getSceneProxy().changeScene(SceneName.ConfigurationChoiceScene);
    }

    /**
     * Should call a scene load on the scene proxy for the configuration creation phase.
     */
    @Override
    public void drawConfigurationLayout() { }

    /**
     * Calls a scene load on the scene proxy for the Waiting phase.
     */
    @Override
    public void drawWaitingLayout() {
        getSceneProxy().changeScene(SceneName.WaitingScene);
    }

    /**
     * Calls a scene load after the starting phase is finished, loads the main screen of the game and puts
     * everything disable until every player has finished its starting phase.
     * Every operation is managed by the scene proxy, which gets the calls and delivers them to the right controller.
     */
    @Override
    public void drawStartingPhaseDone() {
        getSceneProxy().changeScene(SceneName.GameScene);
        getSceneProxy().loadStartingMatch();
        getSceneProxy().disableAll(true);
    }

    /**
     * Calls a scene and cards load for the initial leaders choice.
     * Every operation is managed by the scene proxy, which gets the calls and delivers them to the right controller.
     */
    @Override
    public void drawLeadersChoiceLayout() {
        getSceneProxy().changeScene(SceneName.LeadersChoiceScene);
        getSceneProxy().loadLeaderCards(getClientController().getMatch().getLightPlayer(getClientController().getNickname()).getHandLeaders());
    }

    /**
     * Calls a scene and resources images load for the initial resource/s choice/s.
     * Every operation is managed by the scene proxy, which gets the calls and delivers them to the right controller.
     */
    @Override
    public void drawResourcesChoiceLayout(int yourPosition) {
        getSceneProxy().changeScene(SceneName.StartingResourceScene);
        getSceneProxy().loadStartingResources((yourPosition)/2);
    }

    /**
     * Calls a turn change on the scene proxy.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawYourTurnLayout(boolean yourTurn) {
        getSceneProxy().yourTurn(yourTurn);
    }

    /**
     * Calls a disable pattern for the white marble conversions phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawWhiteMarbleConversionsLayout() {
        getSceneProxy().marketActionPhaseDisables();
    }

    /**
     * Calls a disable pattern for the resources placement phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawResPlacementLayout() {
        getSceneProxy().resourcesPlacementPhaseDisables();
    }

    /**
     * Calls a disable pattern for the development card payment phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawBuyDevCardLayout() {
        getSceneProxy().buyDevActionPhaseDisables();
    }

    /**
     * Calls a disable pattern for the development card placement phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawPlaceDevCardLayout() {
        getSceneProxy().placeDevCardPhaseDisables();
    }

    /**
     * Calls a disable pattern for the production phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawProductionLayout() {
        getSceneProxy().productionActionPhaseDisables();
    }

    /**
     * Calls a confirmButton text modify and a disable pattern on the scene proxy for the end of turn.
     * The operations are managed by the scene proxy, which gets the calls and delivers them to the right controller.
     */
    @Override
    public void drawEndTurnLayout() {
        getSceneProxy().endTurnState();
        getSceneProxy().endTurnPhaseDisables();
    }

    /**
     * Calls a new scene load for the end of match phase.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawEndMatchLayout() {
        getSceneProxy().changeScene(SceneName.EndGameScene);
    }

    /**
     * Calls a new rematch notification visualization.
     * The operation is managed by the scene proxy, which gets the call and delivers it to the right controller.
     */
    @Override
    public void drawRematchOfferLayout(String nickname) {
        getSceneProxy().printRematchOffer(nickname);
    }

    /**
     * Calls a scene load, for server's disconnections handling.
     * It also calls a goodbye message print.
     * The operations are managed by the scene proxy, which gets the calls and delivers them to the right controller.
     * @param msg the goodbye message
     */
    @Override
    public void drawGoodbyeLayout(String msg) {
        getSceneProxy().changeScene(SceneName.GoodbyeScene);
        getSceneProxy().printGoodbye(msg);
    }

    /**
     * Calls a new token's visualization on the scene proxy.
     * The scene proxy delivers the call to the right controller.
     * @param tokenName the name of the token
     * @param remainingTokens the number of tokens left
     */
    @Override
    public void printTokenDraw(String tokenName, int remainingTokens) {
        getSceneProxy().updateTokenDrawn(tokenName, remainingTokens);
    }

    /**
     * Calls a last round flag's visualization method on the scene proxy.
     * The scene proxy delivers the call to the right controller.
     */
    @Override
    public void printLastRound() {
        getSceneProxy().printLastRound();
    }

    /**
     * Calls a setter method on the scene proxy, which will deliver the call to the right controller (TurnScene).
     * @param lastRound the last round flag
     */
    @Override
    public void setLastRound(boolean lastRound) {
        getSceneProxy().setLastRound(lastRound);
    }

    /**
     * Calls a final match ranking visualization's method on the scene proxy.
     * The scene proxy delivers the call to the right controller (RematchPhaseScene).
     * @param message an end match message
     * @param ranking the non-sorted ranking map
     */
    @Override
    public void printMatchResults(String message, Map<String, Integer> ranking) {
        getSceneProxy().printMatchResults(ranking);
    }

    /**
     * Calls a scene proxy's error print method and, eventually graphic reset methods,
     * depending on the current state of the player.
     * The scene proxy delivers the precise call to the right scene controller.
     * @param errMessage the error message
     * @param currentState the state after the unacceptable move (usually the same as before)
     * @param match the match
     */
    @Override
    public void printRetry(String errMessage, StateName currentState, LightMatch match) {
        switch (currentState){
            case LOGIN:
                getSceneProxy().loginError(errMessage);
                break;
            case WAITING_LEADERS:
                getSceneProxy().leadersChoiceError(errMessage);
                break;
            case RESOURCES_PLACEMENT:
                String nickname = getClientController().getNickname();

                getSceneProxy().updateMarketBuffer(nickname, match.getLightPlayer(nickname).getMarketBuffer());
                getSceneProxy().updateWarehouse(nickname, match.getLightPlayer(nickname).getWarehouse());
                getSceneProxy().printRetry(errMessage);
                break;
            case PRODUCTION_ACTION:
            case BUY_DEV_ACTION:
                getSceneProxy().resetPayments();
                getSceneProxy().printRetry(errMessage);
                break;
            case PLACE_DEV_CARD:
                getSceneProxy().resetPlacement();
            default:
                getSceneProxy().printRetry(errMessage);
                break;
        }
    }

    /**
     * Does nothing in GUI.
     * @param player the light player
     */
    @Override
    public void printDiscountMap(LightPlayer player) { }

    /**
     * Does nothing in GUI.
     * @param player the light player
     */
    @Override
    public void printWhiteMarbleConversions(LightPlayer player) { }

    /**
     * Calls a card map setter method on the scene proxy, useful for almost every scene controller to correctly
     * print cards on the scenes and know their abilities (some leaders need to activate an extra shelf, others need to
     * be produced, etc.).
     * @param match the match
     */
    @Override
    public void updateMatch(LightMatch match) {
        getSceneProxy().setCardMap(LightMatch.getCardMap());
    }

    /**
     * Calls an update method for the market.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param match the match
     */
    @Override
    public void updateMarket(LightMatch match) {
        getSceneProxy().updateMarket(match.getMarket());
    }

    /**
     * Calls an update method for the card grid.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param match the match
     */
    @Override
    public void updateCardGrid(LightMatch match) {
        getSceneProxy().updateCardGrid(match.getCardGrid());
    }

    /**
     * Calls an update method for the Lorenzo's marker (in single-player matches).
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param match the match
     */
    @Override
    public void updateLorenzoMarker(LightMatch match) {
        getSceneProxy().updateFaithMarker("Lorenzo the Magnificent", match.getLorenzoMarker());
    }

    /**
     * Calls an update method for the warehouse.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateWarehouse(String nickname, LightMatch match) {
        getSceneProxy().updateWarehouse(nickname, match.getLightPlayer(nickname).getWarehouse());
    }

    /**
     * Calls an update method for the market buffer.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {
        getSceneProxy().updateMarketBuffer(nickname, match.getLightPlayer(nickname).getMarketBuffer());
    }

    /**
     * Calls an update method for the strongbox.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateStrongbox(String nickname, LightMatch match) {
        getSceneProxy().updateStrongBox(nickname, match.getLightPlayer(nickname).getStrongbox());
    }

    /**
     * Calls an update method for the faith marker.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {
        getSceneProxy().updateFaithMarker(nickname, match.getLightPlayer(nickname).getFaithMarker());
    }

    /**
     * Calls an update method for a player's pope tiles' state.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {
        getSceneProxy().updatePopeTiles(nickname, match.getLightPlayer(nickname).getPopeTiles());
    }

    /**
     * Calls an update method for the dev card slots.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {
        getSceneProxy().updateDevCardSlots(nickname, match.getLightPlayer(nickname).getDevCardSlots());
    }

    /**
     * Calls an update method for the hand leaders' state.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {
        getSceneProxy().updateHandLeaders(nickname, match.getLightPlayer(nickname).getHandLeaders());
    }

    /**
     * Calls an update method for a player's active leaders' state.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {
        getSceneProxy().updateActiveLeaders(nickname, match.getLightPlayer(nickname).getActiveLeaders());
    }

    /**
     * Does nothing in GUI.
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) { }

    /**
     * Does nothing in GUI.
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateDiscountMap(String nickname, LightMatch match) { }

    /**
     * Calls an update method for the temporary dev card.
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param match the match
     */
    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {
        getSceneProxy().updateTempDevCard(match.getLightPlayer(nickname).getTempDevCard());
    }

    /**
     * Calls an update method for when a player's connection state changes (goes from online to offline or from offline to online).
     * This operation is handled by the scene proxy, which gets the call and delivers it to the right controller (TurnScene).
     * @param nickname the nickname
     * @param connected the connection value
     */
    @Override
    public void updateDisconnections(String nickname, boolean connected) {
        getSceneProxy().updateDisconnections(nickname, connected);
    }
}