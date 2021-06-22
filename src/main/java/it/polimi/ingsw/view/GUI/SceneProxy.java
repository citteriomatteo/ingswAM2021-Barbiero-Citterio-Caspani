package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.gameLogic.model.essentials.Card;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.network.client.Client.getClient;
import static java.util.Map.entry;

/**
 * Channel for communication between the ClientGUI and the sceneControllers, implementing Proxy and singleton patterns.
 * Every command called on this proxy will be redirected to the right scene controller, if present, using {@link Platform#runLater}.
 * The class contains also some utility methods
 */
public class SceneProxy {
    private static SceneProxy instance;
    private InitSceneController initSceneController;
    private StartingPhaseSceneController startingPhaseSceneController;
    private TurnSceneController turnSceneController;
    private RematchPhaseSceneController rematchPhaseSceneController;
    private GoodbyeSceneController goodbyeSceneController;
    private Map<String, Image> idToImageMap;
    private Map<Image, String> imageToIdMap;
    private SceneName actualScene;
    private Map<Character, Image> charToImageMap;

    /**
     * Private constructor of the SceneProxy, since it is a singleton no one should create an instance of this class.
     */
    private SceneProxy() {
        setMarblesMap();
    }

    /**
     * Gets the instance of the SceneProxy singleton, follows the principle of lazy initialization
     * @return the instance of the SceneProxy singleton
     */
    public static SceneProxy getSceneProxy(){
        if (instance == null)
            instance = new SceneProxy();

        return instance;
    }

    //%%%%%%%%%%%%%%%%%%%%%%%% SETTER %%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Changes the actual SceneController to the InitSceneController passed, removes all the other possible controllers associated
     * @param initSceneController the controller you want to set as the active one
     */
    public void setInitSceneController(InitSceneController initSceneController) {
        this.initSceneController = initSceneController;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;
        this.goodbyeSceneController = null;
    }

    /**
     * Changes the actual SceneController to the StartingPhaseSceneController passed, removes all the other possible controllers associated
     * @param startingPhaseSceneController the controller you want to set as the active one
     */
    public void setStartingPhaseSceneController(StartingPhaseSceneController startingPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = startingPhaseSceneController;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;
        this.goodbyeSceneController = null;

    }

    /**
     * Changes the actual SceneController to the TurnSceneController passed, removes all the other possible controllers associated
     * @param turnSceneController the controller you want to set as the active one
     */
    public void setTurnSceneController(TurnSceneController turnSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = turnSceneController;
        this.rematchPhaseSceneController = null;
        this.goodbyeSceneController = null;

    }

    /**
     * Changes the actual SceneController to the RematchPhaseSceneController passed, removes all the other possible controllers associated
     * @param rematchPhaseSceneController the controller you want to set as the active one
     */
    public void setRematchPhaseSceneController(RematchPhaseSceneController rematchPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = rematchPhaseSceneController;
        this.goodbyeSceneController = null;

    }

    /**
     * Saves on this the GoodbyePhaseSceneController passed
     * @param goodbyeSceneController the controller you want to set as active
     */
    public void setGoodbyePhaseSceneController(GoodbyeSceneController goodbyeSceneController) {

        this.goodbyeSceneController = goodbyeSceneController;
    }

    /**
     * Creates a map that links IDs to the images of the cards and vice versa based on the passed map
     * @param cardMap a map that links IDs with the Card objects ({@link it.polimi.ingsw.gameLogic.model.essentials.DevelopmentCard} and {@link it.polimi.ingsw.gameLogic.model.essentials.leader.LeaderCard})
     *                Dev. Cards Ids have to start with D
     *                Leader Cards Ids have to start with L
     */
    public void setCardMap(Map<String, Card> cardMap){
        InputStream imageStream;
        Image image;

        idToImageMap = new HashMap<>();
        imageToIdMap = new HashMap<>();

        for(String cardId :cardMap.keySet()){
            if((cardId.charAt(0) == 'D' && Integer.parseInt(cardId.substring(1)) > 48) || (cardId.charAt(0) == 'L' && Integer.parseInt(cardId.substring(1)) > 16))
                break;
            imageStream = getClass().getResourceAsStream("images/" +
                        ((cardId.startsWith("L")) ? "leaderCards/" : "developmentCards/front/") + cardId +".png");
            if (imageStream != null) {
                image = new Image(imageStream);
                idToImageMap.put(cardId, image);
                imageToIdMap.put(image, cardId);
            }

        }

        //TODO: modify in case of editor
    }

    /**
     * Builds the map that associate to a character the right image for the market Marbles,
     * the map is then used when {@link SceneProxy#getMarbleImage(char)} is called
     */
    public void setMarblesMap(){
        charToImageMap = Map.ofEntries(
                entry('w', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/whiteMarble.png")))),
                entry('r', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/redMarble.png")))),
                entry('b', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/blueMarble.png")))),
                entry('y', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/yellowMarble.png")))),
                entry('g', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/greyMarble.png")))),
                entry('p', new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/purpleMarble.png"))))
        );

    }

    /**
     * Returns the image of the card associated with the given ID, this should be of the form L* or D*
     * @param ID the unique id of the card
     * @return the relative image or null if there isn't an image associated with that id
     */
    public Image getCardImage(String ID){
        return idToImageMap.get(ID);
    }

    /**
     * Returns the ID of the card associated with the given image
     * @param image the unique image of the card
     * @return the relative ID or null if there isn't an ID associated with that image
     */
    public String getCardID(Image image){
        return imageToIdMap.get(image);
    }

    /**
     * Returns the image of the marble associated with the given char:
     * <ul>
     * <li> w -> white
     * <li> r -> red
     * <li> b -> blue
     * <li> y -> yellow
     * <li> g -> gray
     * <li> p -> purple
     * </ul>
     * @param marble the unique char that identify the marble
     * @return the relative image or null if there isn't an image associated with that char
     */
    public Image getMarbleImage(char marble){
        return charToImageMap.get(marble);
    }

    /**
     * Returns true if the id represents a slot Leader
     * @param cardId the id of the card
     * @return true if the id represents a slot Leader
     */
    public boolean isSlotLeader(String cardId){
        if(!cardId.startsWith("L"))
            return false;
        int number = Integer.parseInt(cardId.substring(1));
        return number >= 5 && number <= 8;
    }

    /**
     * If the scene passed is different from the current one, changes the scene displayed by the main stage with the passed scene
     * @param scene the scene you want to display
     */
    public void changeScene(SceneName scene){
        if(actualScene == scene)
            return;

        actualScene = scene;
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(scene.name());
            } catch (IOException e) {
                e.printStackTrace();
                getClient().exit();
            }
        });
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% RUN_LATER METHODS (PROXY) %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * If the player is playing the game, calls {@link TurnSceneController#loadStartingMatch()}
     */
    public void loadStartingMatch() {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.loadStartingMatch();
        });
    }

    /**
     * If the player is reconnected change scene to LeadersChoiceScene,
     * then calls {@link StartingPhaseSceneController#loadLeaderCards(List)} forwarding the passed parameter
     * @param leaders the list of leaders extracted by the server
     */
    public void loadLeaderCards(List<String> leaders){
        loadSceneIfReconnected(SceneName.LeadersChoiceScene);
        Platform.runLater(()->{
            if(startingPhaseSceneController != null)
                startingPhaseSceneController.loadLeaderCards(leaders);
        });
    }

    /**
     * If the player is reconnected change scene to StartingResourceScene,
     * then calls {@link StartingPhaseSceneController#loadStartingResources(int)} forwarding the passed parameter
     * @param numResources the number of resource that the player has to choose
     */
    public void loadStartingResources(int numResources){
        loadSceneIfReconnected(SceneName.StartingResourceScene);
        Platform.runLater(()->{
            if(startingPhaseSceneController != null)
                startingPhaseSceneController.loadStartingResources(numResources);
        });
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#yourTurn(boolean)}
     * @param yourTurn true if it's the turn of the player
     */
    public void yourTurn(boolean yourTurn) {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.yourTurn(yourTurn);
        });
    }

    //-> %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% DISABLE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#disableAll(boolean)}
     * @param value the value to pass at disableAll method
     */
    public void disableAll(boolean value){
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.disableAll(value));
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#marketActionPhaseDisables()}
     */
    public void marketActionPhaseDisables(){
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.marketActionPhaseDisables());
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#resourcesPlacementPhaseDisables()}
     */
    public void resourcesPlacementPhaseDisables() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.resourcesPlacementPhaseDisables());
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#buyDevActionPhaseDisables()}
     */
    public void buyDevActionPhaseDisables() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.buyDevActionPhaseDisables());
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#placeDevCardPhaseDisables()}
     */
    public void placeDevCardPhaseDisables() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.placeDevCardPhaseDisables());
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#productionActionPhaseDisables()}
     */
    public void productionActionPhaseDisables() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.productionActionPhaseDisables());
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#endTurnPhaseDisables()}
     */
    public void endTurnPhaseDisables() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()-> turnSceneController.endTurnPhaseDisables());
    }

    //-> %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ERROR %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * If the player is in the init phase, calls {@link InitSceneController#loginError(String)}
     * @param errMessage the error message to display
     */
    public void loginError(String errMessage) {
        Platform.runLater(()->{
            if(initSceneController != null)
                initSceneController.loginError(errMessage);
        });
    }

    /**
     * If the player is in the starting phase, calls {@link StartingPhaseSceneController#leadersChoiceError(String)}
     * @param errMessage the error message to display
     */
    public void leadersChoiceError(String errMessage){
        Platform.runLater(()->{
            if (startingPhaseSceneController != null)
                startingPhaseSceneController.leadersChoiceError(errMessage);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#printRetry} forwarding the passed parameter
     * @param errMessage the error message to display
     */
    public void printRetry(String errMessage) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.printRetry(errMessage);
        });
    }

    //-> %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% UPDATE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateCardGrid} forwarding the passed parameter
     * @param cardGrid the new value
     */
    public void updateCardGrid(List<String>[][] cardGrid){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateCardGrid(cardGrid);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateTempDevCard} forwarding the passed parameter
     * @param card the id of the new tempDevCard or null if there isn't a tempDevCard
     */
    public void updateTempDevCard(String card){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateTempDevCard(card);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateMarket} forwarding the passed parameter
     * @param market the new value
     */
    public void updateMarket(char[][] market) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateMarket(market);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateMarketBuffer} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param marketBuffer the new value
     */
    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateMarketBuffer(nickname, marketBuffer);
            });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateWarehouse} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param warehouse the new value
     */
    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateWarehouse(nickname, warehouse);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateHandLeaders} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param handLeaders the new value
     */
    public void updateHandLeaders(String nickname, List<String> handLeaders) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateHandLeaders(nickname, handLeaders);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateActiveLeaders} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param activeLeaders the new value
     */
    public void updateActiveLeaders(String nickname, List<String> activeLeaders) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateActiveLeaders(nickname, activeLeaders);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateFaithMarker} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param faithMarker the new value
     */
    public void updateFaithMarker(String nickname, int faithMarker) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateFaithMarker(nickname, faithMarker);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updatePopeTiles} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param popeTiles the new values
     */
    public void updatePopeTiles(String nickname, List<Integer> popeTiles) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updatePopeTiles(nickname, popeTiles);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateStrongBox} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param strongbox the new value
     */
    public void updateStrongBox(String nickname, List<PhysicalResource> strongbox) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateStrongBox(nickname, strongbox);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateDisconnections} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param connected true if the player is connected, false if it is disconnected
     */
    public void updateDisconnections(String nickname, boolean connected) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateDisconnections(nickname, connected);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateDevCardSlots} forwarding the passed parameters
     * @param nickname the player whom the update is faced to
     * @param devCardSlots the new value
     */
    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateDevCardSlots(nickname, devCardSlots);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#updateTokenDrawn} forwarding the passed parameters
     * @param tokenName the name of the extracted token
     * @param remainingTokens the number of tokens remained in the pile
     */
    public void updateTokenDrawn(String tokenName, int remainingTokens){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateTokenDrawn(tokenName, remainingTokens);
        });
    }

    /**
     * If the player is reconnected change scene to GameScene, then calls {@link TurnSceneController#endTurnState()}
     */
    public void endTurnState() {
        loadSceneIfReconnected(SceneName.GameScene);
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.endTurnState();
        });
    }

    //-> %%%%%%%%%%%%%%%%%%%%%%%%%%% OTHERS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * If the player is playing the game, calls {@link TurnSceneController#resetPayments()}
     */
    public void resetPayments() {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.resetPayments();
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#resetPlacement()}
     */
    public void resetPlacement(){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.resetPlacement();
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#setLastRound} forwarding the passed parameter
     * @param value the new last round value
     */
    public void setLastRound(boolean value) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.setLastRound(value);
        });
    }

    /**
     * If the player is playing the game, calls {@link TurnSceneController#printLastRound()}
     */
    public void printLastRound() {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.printLastRound();
        });
    }

    /**
     * If the player is in the rematch phase, calls {@link RematchPhaseSceneController#printMatchResults} forwarding the passed parameter
     * @param ranking a map that links every player to his score
     */
    public void printMatchResults(Map<String, Integer> ranking) {
        Platform.runLater(()->{
            if(rematchPhaseSceneController != null)
                rematchPhaseSceneController.printMatchResults(ranking);
        });
    }

    /**
     * If the player is in the rematch phase, calls {@link RematchPhaseSceneController#printRematchOffer} forwarding the passed parameter
     * @param nickname the name of the player who has offered a rematch
     */
    public void printRematchOffer(String nickname) {
        Platform.runLater(()->{
            if(rematchPhaseSceneController != null)
                rematchPhaseSceneController.printRematchOffer(nickname);
        });
    }

    /**
     * If the player is in the final phase, calls {@link GoodbyeSceneController#printGoodbyeMessage} forwarding the passed parameter
     * @param msg the message to display
     */
    public void printGoodbye(String msg) {
        Platform.runLater(()->{
            if(goodbyeSceneController != null)
                goodbyeSceneController.printGoodbyeMessage(msg);
        });
    }

    /**
     * Controls if the player is in reconnection, in that case change the scene to the proper one and then controls
     * if the player is in game -> in that case recharge the data
     */
    private void loadSceneIfReconnected(SceneName scene){
        if(initSceneController != null){
                    changeScene(scene);
                    if(actualScene == SceneName.GameScene) {
                        loadStartingMatch();
                        Platform.runLater(()-> turnSceneController.refreshAll() );
                    }
            }
        }

    /**
     * Returns the child of the given father with the given id
     * @param father a pane
     * @param Id the id of the node you want to search for
     * @return the child node or null if there isn't such a child
     */
    public static Node getChildById(Pane father, String Id){
        for(Node child : father.getChildren())
            if(Id.equals(child.getId()))
                return child;
        return null;
    }
}