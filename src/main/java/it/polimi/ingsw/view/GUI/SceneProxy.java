package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.Client.getClient;
import static java.util.Map.entry;


public class SceneProxy {
    private static SceneProxy instance;
    private InitSceneController initSceneController;
    private StartingPhaseSceneController startingPhaseSceneController;
    private TurnSceneController turnSceneController;
    private RematchPhaseSceneController rematchPhaseSceneController;
    private Map<String, Image> idToImageMap;
    private Map<Image, String> imageToIdMap;
    private SceneController actualController;
    private SceneName actualScene;
    private Map<Character, Image> charToImageMap;

    public SceneProxy() {
        setMarblesMap();
    }

    public static SceneProxy getSceneProxy(){
        if (instance == null)
            instance = new SceneProxy();

        return instance;
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%SETTER%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public void setInitSceneController(InitSceneController initSceneController) {
        this.initSceneController = initSceneController;
        this.actualController = initSceneController;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;
    }

    public void setStartingPhaseSceneController(StartingPhaseSceneController startingPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = startingPhaseSceneController;
        this.actualController = startingPhaseSceneController;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;

    }

    public void setTurnSceneController(TurnSceneController turnSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = turnSceneController;
        this.actualController = turnSceneController;
        this.rematchPhaseSceneController = null;

    }

    public void setRematchPhaseSceneController(RematchPhaseSceneController rematchPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = rematchPhaseSceneController;
        this.actualController = rematchPhaseSceneController;

    }

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

    public void setMarblesMap(){
        charToImageMap = Map.ofEntries(
                entry('w', new Image(getClass().getResourceAsStream("images/punchBoard/whiteMarble.png"))),
                entry('r', new Image(getClass().getResourceAsStream("images/punchBoard/redMarble.png"))),
                entry('b', new Image(getClass().getResourceAsStream("images/punchBoard/blueMarble.png"))),
                entry('y', new Image(getClass().getResourceAsStream("images/punchBoard/yellowMarble.png"))),
                entry('g', new Image(getClass().getResourceAsStream("images/punchBoard/greyMarble.png"))),
                entry('p', new Image(getClass().getResourceAsStream("images/punchBoard/purpleMarble.png")))
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

    public void disableAll(boolean value){
        Platform.runLater(()-> {
            turnSceneController.disableAll(value);
        });
    }

    public void marketActionPhaseDisables(){
        Platform.runLater(()-> {
            turnSceneController.marketActionPhaseDisables();
        });
    }
    public void resourcesPlacementPhaseDisables() {
        Platform.runLater(()-> {
            turnSceneController.resourcesPlacementPhaseDisables();
        });
    }
    public void buyDevActionPhaseDisables() {
        Platform.runLater(()-> {
            turnSceneController.buyDevActionPhaseDisables();
        });
    }
    public void placeDevCardPhaseDisables() {
        Platform.runLater(()-> {
            turnSceneController.placeDevCardPhaseDisables();
            turnSceneController.endPaymentsPhase();
        });
    }
    public void productionActionPhaseDisables() {
        Platform.runLater(()-> {
            turnSceneController.productionActionPhaseDisables();
        });
    }
    public void endTurnPhaseDisables() {
        Platform.runLater(()-> {
            turnSceneController.endTurnPhaseDisables();
        });
    }


    public void loadLeaderCards(List<String> leaders){
        Platform.runLater(()->{
            if(startingPhaseSceneController != null)
                startingPhaseSceneController.loadLeaderCards(leaders);
        });
    }

    public void loadStartingResources(int numResources){
        Platform.runLater(()->{
            if(startingPhaseSceneController != null)
                startingPhaseSceneController.loadStartingResources(numResources);
        });
    }

    public void loginError(String errMessage) {
        Platform.runLater(()->{
            if(initSceneController != null)
                initSceneController.loginError(errMessage);
        });
    }

    public void leadersChoiceError(String errMessage){
        Platform.runLater(()->{
            if (startingPhaseSceneController != null)
                startingPhaseSceneController.leadersChoiceError(errMessage);
        });
    }

    public void loadStartingMatch() {
        Platform.runLater(()->{
            if(turnSceneController != null) {
                turnSceneController.loadStartingMatch();
            }
        });
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

    public void yourTurn(boolean yourTurn) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.yourTurn(yourTurn);
        });

    }

    public void updateMarket(char[][] market) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateMarket(market);
        });
    }

    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateMarketBuffer(nickname, marketBuffer);
            });
    }

    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateWarehouse(nickname, warehouse);
        });
    }

    public void printRetry(String errMessage) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.printRetry(errMessage);
        });
    }

    public void endTurnState() {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.endTurnState();
        });
    }

    public void updateHandLeaders(String nickname, List<String> handLeaders) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateHandLeaders(nickname, handLeaders);
        });
    }

    public void updateActiveLeaders(String nickname, List<String> activeLeaders) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateActiveLeaders(nickname, activeLeaders);
        });
    }

    public void updateFaithMarker(String nickname, int faithMarker) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateFaithMarker(nickname, faithMarker);
        });
    }

    public void updateStrongBox(String nickname, List<PhysicalResource> strongbox) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateStrongBox(nickname, strongbox);
        });
    }

    public void updateDisconnections(String nickname, boolean connected) {
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateDisconnections(nickname, connected);
        });
    }

    public void updateCardGrid(List<String>[][] cardGrid){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateCardGrid(cardGrid);
        });
    }

    public void updateTempDevCard(String card){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateTempDevCard(card);
        });
    }

    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateDevCardSlots(nickname, devCardSlots);
        });
    }

    public boolean turnSceneIsLoaded(){
        if(turnSceneController != null)
            return turnSceneController.turnSceneLoaded;
        return false;
    }

    public void updateTokenDrawn(String tokenName, int remainingTokens){
        Platform.runLater(()->{
            if(turnSceneController != null)
                turnSceneController.updateTokenDrawn(tokenName, remainingTokens);
        });
    }

}
