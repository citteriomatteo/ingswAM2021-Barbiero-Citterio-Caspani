package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.model.essentials.Resource;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getChildById;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

/**
 * A scene controller used for communicating with the gameScene
 */
public class TurnSceneController{
    private static final int ENLARGEMENT_CARD = 5;
    private static final int ZOOM_CARD = 25;

    public Pane basePane;
    public ImageView lastRoundFlag;
    public GridPane marketGrid;
    public GridPane cardGrid;
    public ImageView slideMarble;
    public Pane myPane;
    public HBox handLeaders;
    public MenuButton leaderActions1;
    public MenuButton leaderActions2;
    public HBox activeLeaders;
    public HBox marketBufferBox;
    public Pane warehousePane;
    public VBox strongBox;
    public Pane marketPane;
    public Pane paymentsPane;
    public Pane productionPane;
    public GridPane faithPath;
    public VBox enemiesBox;
    public TextArea informationField;
    public Button confirmButton;
    public ImageView tempDevCard;
    public HBox devCardSlots;
    public ImageView basicProduction;
    public ImageView unknownCost1;
    public ImageView unknownCost2;
    public ImageView unknownEarning;
    public ImageView firstProdLeaderUnknown;
    public ImageView secondProdLeaderUnknown;
    public Text countWhite1;
    public Text countWhite2;

    public Pane lorenzoPane;
    public Label lorenzoMarker;
    public ImageView tokenDrawn;
    public Label remainingTokens;

    private final LightMatch match;
    private final LightPlayer player;

    private ResType temporaryRes;
    private Integer firstShelfToSwitch;
    private List<PhysicalResource> chosenResources;
    private Map<Integer, PhysicalResource> paymentsFromWarehouse;
    private List<PhysicalResource> paymentsFromStrongbox;
    private List<PhysicalResource> uCosts;
    private List<Resource> uEarnings;
    private List<String> cardsToProduce;
    private int numUCosts;
    private int numUEarnings;
    private ImageView selectedCard;
    private boolean lastRound;
    private boolean row;
    private int numMarket;
    private List<PhysicalResource> whiteConversions;

    private CtoSMessage message;

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% CONSTRUCTOR %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Creates an instance and links it to the {@link SceneProxy}.
     * Initialize all the temporary variables
     */
    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
        match = getClientController().getMatch();
        player = match.getLightPlayer(getClient().getNickname());

        initializeTemporaryVariables();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%% UTILITY FUNCTIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Brings all the variables useful for inter-state operations inside this to their init values
     */
    private void initializeTemporaryVariables(){
        lastRound = false;
        temporaryRes = null;
        firstShelfToSwitch = null;
        chosenResources = new ArrayList<>();
        paymentsFromWarehouse = new HashMap<>();
        paymentsFromStrongbox = new ArrayList<>();
        cardsToProduce = new ArrayList<>();
        uCosts = new ArrayList<>();
        uEarnings = new ArrayList<>();
        numUCosts = 0;
        numUEarnings = 0;
        selectedCard = null;
        whiteConversions = new ArrayList<>();
    }

    /**
     * Restore all the graphic contents to the values saved in the lightMatch,
     * erase the current temporary message and initialize the temporary variables.
     * It doesn't affect current state
     */
    @FXML
    public void refreshAll(){
        populatePlayerPane(player, myPane);
        boolean foundTempDev = false;
        for(LightPlayer enemy : match.getLightPlayers()) {
            if(enemy != player) {
                Pane enemyPane = (Pane) getChildById(enemiesBox, enemy.getNickname());
                populatePlayerPane(enemy, enemyPane);
                showConnectionState(enemy.isConnected(), enemyPane);
                if(enemy.getTempDevCard() != null) {
                    tempDevCard.setImage(getSceneProxy().getCardImage(enemy.getTempDevCard()));
                    foundTempDev = true;
                }
            }
        }
        if(player.getTempDevCard()!=null)
            tempDevCard.setImage(getSceneProxy().getCardImage(player.getTempDevCard()));
        else if(!foundTempDev)
            tempDevCard.setImage(null);


        message = null;
        resetPayments();
        endProductionPhase();
        closeProductionPane();

        updateMarket(match.getMarket());
        updateCardGrid(match.getCardGrid());

        initializeTemporaryVariables();
    }

    /**
     * Changes the visualization of the player nickname inside the pane watching if it is connected or disconnected
     * @param connected the connection state of the player
     * @param playerPane the pane that represents the player board of the interested player
     */
    private void showConnectionState(boolean connected, Pane playerPane){
        Label label = (Label) SceneProxy.getChildById(playerPane, "nicknameLabel");
        if (connected) {
            label.setText(playerPane.getId());
            label.setTextFill(Color.WHITE);
        }
        else {
            label.setText(playerPane.getId() + " (OFFLINE)");
            label.setTextFill(Color.RED);
        }
    }

    /**
     * Returns the ordinal number of the shelf which contains the 'clicked' Imageview
     * @param clicked the place you want to obtain the related shelf
     * @return the ordinal number of the shelf which contains the 'clicked' Imageview or 0 if it is not part of a shelf
     */
    private int searchShelf(ImageView clicked){
        return warehousePane.getChildren().indexOf(clicked.getParent())+1;
    }

    /**
     * Synchronizes the graphic content of the player pane with the data stored in the {@link LightPlayer}
     * @param player the playerPane's owner
     * @param playerPane the Pane that represents the player board of the player passed
     */
    static public void populatePlayerPane(LightPlayer player, Pane playerPane){
        List<Integer> popeTiles = player.getPopeTiles();
        for(Node n : playerPane.getChildren())
            if(n.getId()!=null)
                switch (n.getId()){
                    case "activeLeaders":
                        setActiveLeadersImages(player.getActiveLeaders(), (HBox) n);
                        break;
                    case "marketBuffer":
                        populateMarketBuffer(player.getMarketBuffer(), (Pane) n);
                        break;
                    case "handLeaders":
                        populateHandLeadersImages(player.getHandLeaders(), (HBox) n);
                        break;
                    case "faithPath":
                        GridPane faithPath = (GridPane) n;
                        for(Node cell : faithPath.getChildren())
                            cell.setVisible(false);
                        faithPath.getChildren().get(player.getFaithMarker()).setVisible(true);
                        break;
                    case "popeTile1":
                        setPopeTileImage(1, popeTiles.get(0), (ImageView)n);
                        break;
                    case "popeTile2":
                        setPopeTileImage(2, popeTiles.get(1), (ImageView)n);
                        break;
                    case "popeTile3":
                        setPopeTileImage(3, popeTiles.get(2), (ImageView)n);
                        break;
                    case "devCardSlots":
                        populateDevCardSlots(player.getDevCardSlots(), (HBox) n);
                        break;
                    case "warehousePane":
                        populateWarehouse(player.getWarehouse(), (Pane) n);
                        break;
                    case "strongBox":
                        populateStrongbox(player.getStrongbox(), (VBox) n);

                }
    }

    /**
     * Sets the correct images for hand leaders either they are explicit or -1
     * @param newHandLeaders the list of ids or -1
     * @param handLeaders the graphic object that shows the leaders
     */
    static public void populateHandLeadersImages(List<String> newHandLeaders, HBox handLeaders) {
        List<Node> leaders = handLeaders.getChildren();
        ImageView leader;
        for (int i = 0; i < leaders.size(); i++) {
            leader = (ImageView) leaders.get(i);
            if(newHandLeaders.size()>i && newHandLeaders.get(0).equals("-1"))
                leader.setImage(new Image(Objects.requireNonNull(TurnSceneController.class.getResourceAsStream("images/leaderCards/leaderCardBack.png"))));
            else if (newHandLeaders.size()>i)
                ((ImageView) handLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(newHandLeaders.get(i)));
            else
                leader.setImage(null);
        }
    }

    /**
     * Sets the correct images for active leaders if they are present, empties the imageView otherwise
     * @param newActiveLeaders the list of ids that represents the active leaders
     * @param activeLeaders the graphic object that shows the leaders
     */
    static public void setActiveLeadersImages(List<String> newActiveLeaders, HBox activeLeaders) {
        if(newActiveLeaders.size()==2 && !getSceneProxy().isSlotLeader(newActiveLeaders.get(0)) && getSceneProxy().isSlotLeader(newActiveLeaders.get(1)))
            Collections.swap(newActiveLeaders, 0, 1);

        List<Node> leaders = activeLeaders.getChildren();
        for (int i = 0; i <leaders.size(); i++) {
            ImageView leader = (ImageView) leaders.get(i);
            if(newActiveLeaders.size() > i)
                leader.setImage(getSceneProxy().getCardImage(newActiveLeaders.get(i)));
            else
                leader.setImage(null);
        }
    }

    /**
     * Places the right images on the right slot of the marketBufferBox
     * @param marketBuffer the values of the effective marketBuffer
     * @param marketBufferBox the graphic object that represents the market buffer
     */
    static public void populateMarketBuffer(List<PhysicalResource> marketBuffer, Pane marketBufferBox) {
        List<Node> places = marketBufferBox.getChildren();
        for (int i = 0; i < places.size(); i++) {
            if(i < marketBuffer.size())
                ((ImageView) places.get(i)).setImage(marketBuffer.get(i).getType().asImage());
            else
                ((ImageView) places.get(i)).setImage(null);
        }
    }

    /**
     * Sets the correct images for the devCardSlots, if a card is present sets its image, otherwise sets null that image
     * @param devCardSlots the array of columns that occupies the devCardSlots, the values inside the lists are the ids of the cards
     * @param devCardHBox the graphic object that shows the devCardSlots
     */
    static public void populateDevCardSlots(List<String>[] devCardSlots, HBox devCardHBox) {
        List<String> cardColumn;
        ImageView imageview;
        StackPane cardColumnStack;
        for (int i = 0; i < devCardSlots.length; i++) { //for every column
            cardColumn = devCardSlots[i];
            cardColumnStack = (StackPane) devCardHBox.getChildren().get(i);
            for (int j = 0; j < cardColumnStack.getChildren().size(); j++) { //for every imageView inside the Stack
                imageview = (ImageView) cardColumnStack.getChildren().get(j);
                if(j<cardColumn.size())
                    imageview.setImage(getSceneProxy().getCardImage(cardColumn.get(j)));
                else {
                    imageview.setImage(null);
                    imageview.setDisable(true);
                }
            }
        }
    }

    /**
     * Places the right images on the right slot of the warehousePane
     * @param warehouse the values of the effective warehouse
     * @param warehousePane the graphic object that represents the warehouse
     */
    static public void populateWarehouse(List<PhysicalResource> warehouse, Pane warehousePane) {
        for (int i = 0; i < warehouse.size(); i++) {
            PhysicalResource resShelf = warehouse.get(i);
            HBox shelf = (HBox) warehousePane.getChildren().get(i);
            for (int j = 0; j < resShelf.getQuantity(); j++) {
                ((ImageView) shelf.getChildren().get(j)).setImage(resShelf.getType().asImage());
            }
        }
    }

    /**
     * Writes the right number of resource for every type inside the labels of the strongboxVbox
     * @param newStrongbox the values of the effective strongbox
     * @param strongboxVbox the graphic object that represents the strongbox
     */
    static public void populateStrongbox(List<PhysicalResource> newStrongbox, VBox strongboxVbox) {
        for (PhysicalResource resource : newStrongbox) {
            for (Node n : strongboxVbox.getChildren()) {
                HBox shelf = (HBox) n;
                if (resource.getType().toString().toLowerCase().equals(shelf.getId()))
                    ((Text) shelf.getChildren().get(1)).setText("x" + resource.getQuantity());
            }
        }
    }

    /**
     * Places the right images for the pope tiles inside the interestedPane, this could be myPane or an enemy pane
     * @param popeTiles the values of the effective popeTiles (0 -> the tile is absent
     *                                                         1 -> the tile is upside
     *                                                         2 -> the tile is downside)
     * @param interestedPane the graphic object that contains a playerBoard
     */
    static public void populatePopeTiles(List<Integer> popeTiles, Pane interestedPane) {
        for(Node n : interestedPane.getChildren())
            if(n.getId()!= null)
                switch (n.getId()){
                    case "popeTile1":
                        setPopeTileImage(1, popeTiles.get(0), (ImageView)n);
                        break;
                    case "popeTile2":
                        setPopeTileImage(2, popeTiles.get(1), (ImageView)n);
                        break;
                    case "popeTile3":
                        setPopeTileImage(3, popeTiles.get(2), (ImageView)n);
                        break;
                }
    }

    /**
     * Places the right image for the popeTileView passed
     * @param order the order of the pope tile, from the start of the faith path (the first has order 1)
     * @param value the value of the effective popeTile (0 -> the tile is absent
     *                                                   1 -> the tile is upside
     *                                                   2 -> the tile is downside)
     * @param popeTileView the graphic object that contains a popeTile image
     */
    static public void setPopeTileImage(int order, int value, ImageView popeTileView){
        Image tile;
        if(value == 1) //tile is upside
            tile = new Image(Objects.requireNonNull(TurnSceneController.class.getResourceAsStream("images/punchBoard/upsidePopeTile" + (order) + ".png")));
        else if(value == 2) //tile is downside
            tile = new Image(Objects.requireNonNull(TurnSceneController.class.getResourceAsStream("images/punchBoard/PopeTile" + (order) + ".png")));
        else
            tile = null;

        popeTileView.setImage(tile);
    }

    /**
     * Modifies the passed StackPane according to the passed values (the passed stackPane should only contain ImageViews),
     * disables the imageViews under the depth newDepth, changes the image with that precise depth and remove all
     * the imageViews that goes over that depth
     * @param stackPane the stackPane you want to modify
     * @param newDepth the new depth of the stack
     * @param topCard the Id of the card that should be on the top of the stack
     */
    private void setStackPaneContent(StackPane stackPane, int newDepth, String topCard) {
        ImageView imageView;
        List<Node> cards = stackPane.getChildren();
        for (int k = cards.size()-1; k >= 0; k--) {
            imageView = (ImageView) cards.get(k);
            if(imageView.getEffect() != null) {
                imageView.setEffect(null);
                imageView.setFitWidth(imageView.getFitWidth() - ENLARGEMENT_CARD);
                imageView.setFitHeight(imageView.getFitHeight() - ENLARGEMENT_CARD);
            }
            if (k < newDepth)
                imageView.setDisable(true);
            else if (k == newDepth) {
                imageView.setImage(getSceneProxy().getCardImage(topCard));
                imageView.setDisable(false);
            } else {
                stackPane.getChildren().remove(imageView);
            }
        }
    }

    /**
     * Sets the text on the button Confirm to EndTurn
     */
    public void endTurnState() {
        confirmButton.setText("End turn");
    }

    /**
     * Resets the situation if the payments has gone wrong or are finished ->
     * recharges the warehouse and strongbox watching the one saved on the lightPlayer
     * and restore the payments pane to his starting situation
     */
    public void resetPayments() {
        VBox resourcesVBox = (VBox) paymentsPane.getChildren().get(2);
        for(Node n : resourcesVBox.getChildren()){
            HBox resourceHBox = (HBox) n;
            Label resourceLabel = (Label) resourceHBox.getChildren().get(1);
            resourceLabel.setText("x0");
        }
        updateWarehouse(player.getNickname(), player.getWarehouse());
        updateStrongBox(player.getNickname(), player.getStrongbox());
    }

    /**
     * Resets the situation if the placement of a card has gone wrong ->
     * recharges the devCardSlots watching the one saved on the lightPlayer and eventually restore the tempDevCard
     */
    public void resetPlacement(){
        populateDevCardSlots(player.getDevCardSlots(), devCardSlots);
        if(player.getTempDevCard()!=null)
            tempDevCard.setImage(getSceneProxy().getCardImage(player.getTempDevCard()));
        placeDevCardPhaseDisables();
    }

    /**
     * Returns the type of resource the passed leader can convert the white marble to
     * @param leaderId the id of the leader you want to know the power conversion
     * @return the type of resource the passed leader can convert the white marble to or null if the leader isn't a WhiteMarbleLeader
     */
    private ResType getWhiteConversion(String leaderId){
        ResType conversion = null;
        switch (leaderId) {
            case "L9":
                conversion = ResType.SERVANT;
                break;
            case "L10":
                conversion = ResType.SHIELD;
                break;
            case "L11":
                conversion = ResType.STONE;
                break;
            case "L12":
                conversion = ResType.COIN;
                break;
        }

        return conversion;
    }


//%%%%%%%%%%%%%%%%%%%%%% ONE TIME FUNCTION %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Prepare the scene before the starts of the game: sets the id of the players' boards and makes invisible the remaining boards.
     * Than calls the updates to loads all the elements in the scene, including the personalBoard of every player.
     * This methods is also call in case of reconnection.
     */
    public void loadStartingMatch() {
        List<LightPlayer> enemies = new ArrayList<>(match.getLightPlayers());
        enemies.remove(player);

        int k;
        for (k=0; k < enemies.size(); k++) {
            Pane enemyPane = (Pane) enemiesBox.getChildren().get(k);
            enemyPane.setId(enemies.get(k).getNickname());
            Label enemyName = (Label) SceneProxy.getChildById(enemyPane, "nicknameLabel");
            enemyName.setText(enemies.get(k).getNickname());
        }

        //in order to put invisible the boards of the remaining free players' positions.
        for(int h = k; h<3; h++)
            enemiesBox.getChildren().get(h).setVisible(false);

        //put visible the pane about Lorenzo in case of a single-player match
        if(match.getLightPlayers().size() == 1) {
            lorenzoPane.setVisible(true);
        }

        for(Node n : cardGrid.getChildren()){
            List<Node> images = ((StackPane) n).getChildren();
            for (int i=0; i < images.size()-1; i++)
                images.get(i).setDisable(true);
        }

        updateMarket(match.getMarket());

        updateCardGrid(match.getCardGrid());

        updateHandLeaders(player.getNickname(), player.getHandLeaders());
        updateMarketBuffer(player.getNickname(), player.getMarketBuffer());

        for (int i = 0; i < strongBox.getChildren().size(); i++) {
            HBox resHBox = (HBox) strongBox.getChildren().get(i);
            ResType[] values = ResType.values();
            ((ImageView) resHBox.getChildren().get(0)).setImage(values[i+1].asImage());

        }

        for (LightPlayer player1 : match.getLightPlayers()) {
            updateWarehouse(player1.getNickname(), player1.getWarehouse());
            updateFaithMarker(player1.getNickname(), player1.getFaithMarker());
            updateStrongBox(player1.getNickname(), player1.getStrongbox());
        }

        //for the inkwell:
        assignInkwell();

        //for the last round flag in reconnections:
        if(lastRound)
            printLastRound();
    }

    /**
     * This method looks for the first player and puts visible his inkwell.
     * It's called once at the start of TurnSceneController's lifecycle.
     */
    private void assignInkwell() {
        String firstPlayer = match.getLightPlayers().get(0).getNickname();

        if(firstPlayer.equals(player.getNickname()))
            SceneProxy.getChildById(myPane, "inkwell").setVisible(true);

        else
            for(Node box : enemiesBox.getChildren())
                if(firstPlayer.equals(box.getId()))
                    SceneProxy.getChildById((Pane) box, "inkwell").setVisible(true);
    }


//%%%%%%%%%%%%%%%%%%%%%% DISABLE FUNCTIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Disables or enables common things and the player's pane.
     * @param value disable boolean value
     */
    public void disableAll(boolean value) {
        marketPane.setDisable(value);
        cardGrid.setDisable(value);
        tempDevCard.setDisable(value);
        myPane.setDisable(value);
        if(value)
            for(Node n : myPane.getChildren())
                n.setDisable(false);
    }

    /**
     * Keeps enabled only the requested elements inside the player pane and disables everything else inside the pane.
     * Confirm button always remain activated
     * @param ids list of chosen elements' ids
     */
    public void keepEnabledOnly(List<String> ids){
        ids.add("confirmButton");
        myPane.setDisable(false);
        for(Node n : myPane.getChildren())
            n.setDisable(!ids.contains(n.getId()));
    }

    /**
     * Sets a disable pattern for the white marble conversions phase.
     * In this phase, only warehouse (for switches) and active leaders must be enabled.
     */
    public void marketActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        tempDevCard.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "activeLeaders"));

        keepEnabledOnly(importantIds);

        for(Node n : myPane.getChildren())
            //putting visible the labels for the white marble conversions
            if(("firstConversionCount").equals(n.getId()) || ("secondConversionCount").equals(n.getId()))
                n.setVisible(true);

        //information field text setting for this state
        informationField.appendText("\nConvert each white marble.");
    }

    /**
     * Sets a disable pattern for the resources placement phase.
     * In this phase, only warehouse and market buffer must be enabled (for switches and insertions).
     */
    public void resourcesPlacementPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        tempDevCard.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "marketBufferBox"));

        keepEnabledOnly(importantIds);

        for(Node n : myPane.getChildren())
            //putting back invisible the labels for the white marble conversions
            if(("firstConversionCount").equals(n.getId()) || ("secondConversionCount").equals(n.getId()))
                n.setVisible(false);

        //information field text setting for this state
        informationField.appendText("\nPlace the resources you've got.");
    }

    /**
     * Sets a disable pattern for the development card payments phase.
     * In this phase, only the payments pane, warehouse and strongbox must be enabled (to drag and drop resources
     * from a resources storage to the pane).
     */
    public void buyDevActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        paymentsPane.setDisable(false);
        paymentsPane.setVisible(true);
        tempDevCard.setDisable(false);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "strongbox"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.appendText("\nPay the card you've chosen.");
    }

    /**
     * Sets a disable pattern for the development card placement phase.
     * In this phase, only the warehouse (for switches), the temporary dev card slot and the dev card slots
     * must be enabled (to drag and drop the card into a slot).
     */
    public void placeDevCardPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        tempDevCard.setDisable(false);
        endPaymentsPhase();

        for(Node node : devCardSlots.getChildren()){
            StackPane stackPane = (StackPane) node;
            int firstFree = findFreePosition(stackPane);
            if(firstFree >= 0)
                stackPane.getChildren().get(firstFree).setDisable(false);

        }

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane","devCardSlots", "1DevSlot", "2DevSlot", "3DevSlot"));
        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.appendText("\nPlace your Development card.");
    }

    /**
     * Sets a disable pattern for the production phase.
     * In this phase, only the payments pane, the resources storage and the dev card slots must be enabled
     * (in order to select cards to produce and have the chance to pay them).
     */
    public void productionActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        paymentsPane.setDisable(false);
        paymentsPane.setVisible(true);
        tempDevCard.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "strongbox","devCardSlots", "1DevSlot", "2DevSlot", "3DevSlot"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.appendText("\nProduce.");

    }

    /**
     * Sets a disable pattern for the end of turn phase.
     * In this phase, only the warehouse (for switches) and the leaders menuBars (for activations/discards) must be enabled.
     */
    public void endTurnPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        tempDevCard.setDisable(true);
        endPaymentsPhase();
        endProductionPhase();

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "leaderActions1", "leaderActions2"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.appendText("\nIt's the end of your turn. Switch shelves, discard/activate leaders or finish the turn.");

    }

    /**
     * Resets the payments pane for the next payment (setting all labels to "x0"),
     * then sets invisible and disables the pane.
     */
    private void endPaymentsPhase() {
        VBox resourcesVBox = (VBox) paymentsPane.getChildren().get(2);
        for(Node n : resourcesVBox.getChildren()){
            HBox resourceHBox = (HBox) n;
            Label resourceLabel = (Label) resourceHBox.getChildren().get(1);
            resourceLabel.setText("x0");
        }
        paymentsPane.setDisable(true);
        paymentsPane.setVisible(false);
    }

    /**
     * Removes all the images of the resources chosen for converting the unknowns and
     * removes all the glow effects on the cards produced
     */
    private void endProductionPhase(){
        basicProduction.setEffect(null);
        unknownCost1.setImage(null);
        unknownCost2.setImage(null);
        unknownEarning.setImage(null);

        for(Node node : activeLeaders.getChildren()){
            ImageView leader = (ImageView) node;
            leader.setEffect(null);
        }

        activeLeaders.setDisable(true);
        firstProdLeaderUnknown.setImage(null);
        secondProdLeaderUnknown.setImage(null);

        for(Node node : devCardSlots.getChildren()){
            StackPane stackPane = (StackPane) node;
            for(Node n : stackPane.getChildren()){
                ImageView devCard = (ImageView) n;
                devCard.setEffect(null);
            }
        }
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%  LEADERS POWER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Activate the power of the clicked leader only if it's a leader that convert the white marbles
     * or if it is a leader that can start a production.
     * In the first case adds the conversion to the list of conversion and if the list is full decrease the count of the conversions of the other type,
     * after that create a message ({@link WhiteMarblesConversionMessage}).
     * In the second case calls the relative function.
     * @param mouseEvent the mouseEvent that calls the method.
     */
    @FXML
    public void useLeader(MouseEvent mouseEvent) {
        if(getClientController().getCurrentState().equals(StateName.MARKET_ACTION)){
            int numWhiteDrawn = match.numWhiteDrawn(row, numMarket);
            Node leader = (Node) mouseEvent.getSource();
            int numConverted = Integer.parseInt(countWhite1.getText().substring(1)) + Integer.parseInt(countWhite2.getText().substring(1));

            String leaderId = getSceneProxy().getCardID(((ImageView) mouseEvent.getSource()).getImage());

           if(Integer.parseInt((String) leader.getUserData()) == 1){
               useWhiteLeader(numWhiteDrawn, numConverted, leaderId, countWhite1, countWhite2);

           }
           else {
               useWhiteLeader(numWhiteDrawn, numConverted, leaderId, countWhite2, countWhite1);
           }

           message = new WhiteMarblesConversionMessage(player.getNickname(), whiteConversions);
        }
        else if(getClientController().getCurrentState().equals(StateName.STARTING_TURN))
            produce(mouseEvent);
    }

    /**
     * Adds the chosen conversion to the list of white marbles conversions and increase the count on the relative Text.
     * If the list is full decrease the count of the conversions of the other type.
     * @param numWhiteDrawn the number of white marbles drawn.
     * @param numConverted the number of conversions already chosen.
     * @param leaderId the id of the selected leader.
     * @param countWhite1 the text field associated to the clicked leader.
     * @param countWhite2 the text field associated to the other white leader.
     */
    private void useWhiteLeader(int numWhiteDrawn, int numConverted, String leaderId, Text countWhite1, Text countWhite2) {
        if(Integer.parseInt(countWhite1.getText().substring(1)) < numWhiteDrawn) {
            countWhite1.setText("x" + (Integer.parseInt(countWhite1.getText().substring(1)) + 1));
            whiteConversions.add(new PhysicalResource(getWhiteConversion(leaderId), 1));
        }

        if(numConverted == numWhiteDrawn && Integer.parseInt(countWhite2.getText().substring(1)) > 0) {
            for (PhysicalResource conversion : whiteConversions)
                if(!conversion.getType().equals(getWhiteConversion(leaderId))) {
                    whiteConversions.remove(conversion);
                    break;
                }
            countWhite2.setText("x" + (Integer.parseInt(countWhite2.getText().substring(1))-1));
        }
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%  START / END TURN FUNCTIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Sets the right text on informationField and confirmButton.
     * Disables all the panes you can't use in this phase.
     * @param yourTurn true if it's your turn, false if it's not.
     */
    public void yourTurn(boolean yourTurn) {
        informationField.clear();
        if (yourTurn)
            informationField.appendText("\nIt's your turn! Make a move.");
        else
            informationField.appendText("\nWait for your turn.");

        confirmButton.setText("Confirm");
        disableAll(!yourTurn);
    }

    /**
     * When called two times, sends a SwitchShelfMessage asking for a switch between the two sources that has called this method
     * @param mouseEvent the event that triggered this method
     */
    @FXML
    public void switchShelf(MouseEvent mouseEvent) {
        ImageView clicked = (ImageView) mouseEvent.getSource();
        int shelf = searchShelf(clicked);
        if (shelf > 3 && shelf > player.getWarehouse().size()) {
            firstShelfToSwitch = null;
            return;
        }

        clicked.getParent().setEffect(new Glow(0.3));

        if (firstShelfToSwitch == null) {
            firstShelfToSwitch = shelf;
            return;
        }

        (new SwitchShelfMessage(player.getNickname(), firstShelfToSwitch, shelf)).send();
        clicked.getParent().setEffect(null);
        warehousePane.getChildren().get(firstShelfToSwitch-1).setEffect(null);
        firstShelfToSwitch = null;

        mouseEvent.consume();

    }

    /**
     * Create a message ({@link LeaderActivationMessage} with the id of the leader to activate.
     * @param actionEvent the actionEvent who calls the method
     */
    @FXML
    public void activateLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());
        String cardId = getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage());

        (new LeaderActivationMessage(player.getNickname(), cardId)).send();

        actionEvent.consume();
    }

    /**
     * Removes the image of the selected leader and create a message ({@link LeaderDiscardingMessage} with the id of the leader to discard.
     * @param actionEvent the actionEvent who calls the method
     */
    @FXML
    public void discardLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());

        (new LeaderDiscardingMessage(player.getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()))).send();

        /*
        manually removing the hand leaders from my hand (no confirm needed by the server) and setting invisible the
            related MenuButton.
        */
        player.getHandLeaders().remove(getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()));
        updateHandLeaders(player.getNickname(), player.getHandLeaders());

        deleteLeaderMenu();
        actionEvent.consume();
    }

    /**
     * Sets invisible and disabled the leaderAction menu if there is no more a leader in that position
     */
    private void deleteLeaderMenu(){
        if(player.getHandLeaders().size() <= 1){
            leaderActions2.setDisable(true);
            leaderActions2.setVisible(false);
            if(player.getHandLeaders().size() == 0){
                leaderActions1.setDisable(true);
                leaderActions1.setVisible(false);
            }
        }
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%% MARKET DRAW %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Creates a message ({@link MarketDrawMessage}) to draw the chosen column.
     * @param mouseEvent the mouseEvent who calls the method
     */
    @FXML
    public void columnCall(MouseEvent mouseEvent) {
        int numColumn = marketDraw(mouseEvent);
        row = false;
        numMarket = numColumn;

        message = new MarketDrawMessage(player.getNickname(), row, numColumn);
        mouseEvent.consume();
    }

    /**
     * Sets a glow effect on the imageView of the arrow associated with the chosen draw/column
     * and removes all the effects on the other arrows.
     * Returns the number of the chosen row/column.
     * @param mouseEvent the mouseEvent that starts the sequence.
     * @return the number of the chosen row/column.
     */
    private int marketDraw(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        String data = (String) node.getUserData();
        int numSelected = Integer.parseInt(data);

        for (Node child : marketPane.getChildren()) {
            if(!child.getId().equals(marketGrid.getId()))
                child.setEffect(null);
        }

        node.setEffect(new Glow(1));

        return numSelected;
    }

    /**
     * Creates a message ({@link MarketDrawMessage}) to draw the chosen row.
     * @param mouseEvent the mouseEvent who calls the method
     */
    @FXML
    public void rowCall(MouseEvent mouseEvent) {
        int numRow = marketDraw(mouseEvent);
        row = true;
        numMarket = numRow;

        message = new MarketDrawMessage(player.getNickname(), row, numRow);
        mouseEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%% DEV CARD BUY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Enlarges the clicked card and sets a temporary message {@link DevCardDrawMessage} for the selected card
     * If you had previously clicked on another card changes the message and the visualization of the previous and the new clicked card
     * @param mouseEvent the event that triggered this method
     */
    @FXML
    public void drawDevCard(MouseEvent mouseEvent) {
        ImageView card = (ImageView) mouseEvent.getSource();
        if(selectedCard != null) {
            selectedCard.setEffect(null);
            selectedCard.setFitWidth(card.getFitWidth());
            selectedCard.setFitHeight(card.getFitHeight());
        }

        if(!card.equals(selectedCard)) {
            selectedCard = card;
            card.setEffect(new Glow(0.3));
            card.setFitWidth(card.getFitWidth() + ENLARGEMENT_CARD);
            card.setFitHeight(card.getFitHeight() + ENLARGEMENT_CARD);
        }

        int rowIndex, columnIndex;
        rowIndex = GridPane.getRowIndex(card.getParent());
        columnIndex = GridPane.getColumnIndex(card.getParent());

        message = new DevCardDrawMessage(player.getNickname(), cardGrid.getRowCount()-rowIndex, columnIndex + 1);
        mouseEvent.consume();

    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% DEV CARD PLACEMENT %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Starts the drag and drop sequence from the tempDevCard
     * @param mouseEvent the event that triggered this method
     */
    @FXML
    public void dragCard(MouseEvent mouseEvent) {
        Dragboard db = tempDevCard.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage((new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/handCard.png")))));
        db.setContent(content);

        //Avoid behind objects to detect this event
        mouseEvent.consume();
    }

    /**
     * If you can drop the card here({@link DragEvent#getSource()}) sets a temporary message {@link DevCardPlacementMessage} with the relative column.
     * It doesn't control if you can place effectively the card but only if there is still space in the stack
     * @param dragEvent the event that triggered this method
     */
    @FXML
    public void dropCard(DragEvent dragEvent) {
        boolean success;
        StackPane selectedColumn = (StackPane) ((Node)dragEvent.getSource()).getParent();
        int freePosition = findFreePosition(selectedColumn);
        if(freePosition>=0) {
            success = true;
            ((ImageView)selectedColumn.getChildren().get(freePosition)).setImage(tempDevCard.getImage());

            message = new DevCardPlacementMessage(player.getNickname(), devCardSlots.getChildren().indexOf(selectedColumn)+1);
        }
        else
            success = false;

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * Return the first free position in the stackPane, if there isn't a free position, returns -1
     * @param selectedColumn the stack pane you want to search into
     * @return the first free position in the stackPane, if there isn't a free position, returns -1
     */
    private int findFreePosition(StackPane selectedColumn) {
        for (int i = 0; i < selectedColumn.getChildren().size(); i++) {
            if(((ImageView)selectedColumn.getChildren().get(i)).getImage() == null)
                return i;
        }
        return -1;
    }

    /**
     * If the drag and drop sequence starts from the TempDevCard, accept the drop
     * @param dragEvent the event that triggered this method
     */
    @FXML
    public void acceptCardDrop(DragEvent dragEvent) {
        Node gestureSource = (Node)dragEvent.getGestureSource();
        if(tempDevCard.equals(gestureSource))
            dragEvent.acceptTransferModes(TransferMode.MOVE);

        dragEvent.consume();
    }

    /**
     * Enlarges the dimensions of the ImageView source
     * @param mouseEvent the event that triggered this method
     */
    @FXML
    public void zoomInCard(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        imageView.setFitWidth(imageView.getFitWidth() + ZOOM_CARD);
        imageView.setFitHeight(imageView.getFitHeight() + ZOOM_CARD);
        imageView.toFront();

        mouseEvent.consume();
    }

    /**
     * Reduce the dimensions of the ImageView source
     * @param mouseEvent the event that triggered this method
     */
    @FXML
    public void zoomOutCard(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        imageView.setFitWidth(imageView.getFitWidth() - ZOOM_CARD);
        imageView.setFitHeight(imageView.getFitHeight() - ZOOM_CARD);

        mouseEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PRODUCTION %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Adds the chosen card ({@link MouseEvent#getSource()}) to the list of cards to produce if it is not already contained, adds a glow effect on the card,
     * and increases numEarnings if the card chosen is a leader card.
     * Otherwise if the card is already on the list, the method removes it, removes the glow effect and removes the chosen conversions if they exist.
     * Finally the method creates a message ({@link ProductionMessage})
     * @param mouseEvent that calls the function
     */
    @FXML
    public void produce(MouseEvent mouseEvent) {
        ImageView imageViewCard = (ImageView) mouseEvent.getSource();
        String selectedCard = getSceneProxy().getCardID(imageViewCard.getImage());

        if(selectedCard == null)
            return;

        if(selectedCard.charAt(0) == 'D') {
            if(!cardsToProduce.contains(selectedCard)) {
                cardsToProduce.add(selectedCard);
                imageViewCard.setEffect(new Glow(0.5));
            }
            else {
                imageViewCard.setEffect(null);
                cardsToProduce.remove(selectedCard);
            }
        }
        else {
            if(!cardsToProduce.contains(selectedCard)){
                int numCard = Integer.parseInt(selectedCard.substring(1));
                if(numCard < 13) {
                    return;
                }
                cardsToProduce.add(selectedCard);
                imageViewCard.setEffect(new Glow(0.5));
                numUEarnings += 1;
                productionPane.setDisable(false);
                productionPane.setVisible(true);
                productionPane.getChildren().get(0).setVisible(false);
                productionPane.getChildren().get(1).setVisible(false);
                productionPane.getChildren().get(1).setDisable(true);
            }
            else{
                imageViewCard.setEffect(null);
                int numInArray = cardsToProduce.indexOf(selectedCard) + 1;

                int numUEarningsToProduce = 0;
                int actualUEarnings = 0;
                for (String card : cardsToProduce) {
                    if (card.startsWith("L") || card.startsWith("B"))
                        numUEarningsToProduce++;
                }

                for (Resource uEarning : uEarnings)
                    actualUEarnings += uEarning.getQuantity();

                cardsToProduce.remove(selectedCard);
                numUEarnings --;
                removeResource(numInArray, numUEarningsToProduce, actualUEarnings);

                closeProductionPane();

                if(getSceneProxy().getCardID(((ImageView) activeLeaders.getChildren().get(0)).getImage()).equals(selectedCard))
                    firstProdLeaderUnknown.setImage(null);
                else
                    secondProdLeaderUnknown.setImage(null);
            }

        }

        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));

        mouseEvent.consume();
    }

    /**
     * Removes the resource associate to the card you want to deselect.
     * @param numInArray the position of the card in the list.
     * @param numUEarningsToProduce the total number of earnings to choose
     * @param actualUEarnings the number of chosen earnings
     */
    private void removeResource(int numInArray, int numUEarningsToProduce, int actualUEarnings) {
        int count = 0;
        if(numUEarningsToProduce == actualUEarnings){
            for (Resource uEarning : uEarnings){
                count += uEarning.getQuantity();
                if(count > numInArray) {
                    uEarnings.remove(uEarning);
                    if (uEarning.getQuantity() > 1){
                        uEarning = new PhysicalResource(((PhysicalResource) uEarning).getType(), uEarning.getQuantity() - 1);
                        uEarnings.add(uEarning);
                    }

                    break;
                }

            }
        }
    }

    /**
     * Adds "BASICPROD" to the list of cards to produce if it is not already contained, adds a glow effect on the card,
     * and increases numUCosts and numUEarnings.
     * Otherwise if the card is already on the list, the method removes it, removes the glow effect and removes the chosen conversions if they exist.
     * Finally the method creates a message ({@link ProductionMessage})
     */
    @FXML
    public void produceBasicProd() {
        int index = cardsToProduce.indexOf("BASICPROD");

        if(index != -1) {
            int numUofLeaders = 0;
            int numUEarningsToProduce = 0;
            int actualUEarnings = 0;
            boolean found = false;
            for (String card : cardsToProduce) {
                if (card.startsWith("L")) {
                    numUEarningsToProduce++;
                    if(!found)
                        numUofLeaders++;
                }
                else if(card.startsWith("B")){
                    numUEarningsToProduce++;
                    found = true;
                }
            }

            for (Resource uEarning : uEarnings)
                actualUEarnings += uEarning.getQuantity();

            cardsToProduce.remove("BASICPROD");
            basicProduction.setEffect(null);
            unknownCost1.setImage(null);
            unknownCost2.setImage(null);
            unknownEarning.setImage(null);

            uCosts = new ArrayList<>();

            removeResource(numUofLeaders, numUEarningsToProduce, actualUEarnings);

            closeProductionPane();

            numUEarnings --;
            numUCosts -= 2;

        }
        else {
            numUCosts += 2;
            numUEarnings += 1;
            cardsToProduce.add("BASICPROD");
            basicProduction.setEffect(new Glow(0.5));
            productionPane.setDisable(false);
            productionPane.setVisible(true);
            productionPane.getChildren().get(0).setVisible(true);
            productionPane.getChildren().get(1).setVisible(true);
            productionPane.getChildren().get(1).setDisable(false);
        }


        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));
    }

    private PhysicalResource extractSelectedResource(ImageView selectedResourceImageView){
        ResType selectedType = ResType.UNKNOWN;
        for (ResType type : ResType.values())
            if(type.toString().toLowerCase().equals(selectedResourceImageView.getId())) {
                selectedType = type;
                break;
            }

        return new PhysicalResource(selectedType, 1);
    }

    /**
     * Increase the count on the label of the clicked resource ({@link MouseEvent#getSource()})
     * and adds the resource to the list of conversions for unknown costs.
     * It also put on the imageView of the unknown cost of the card the image of the chosen resource.
     * If already exists a conversion for the selected unknown cost it replace it.
     * @param mouseEvent the mouseEvent that calls the method.
     */
    @FXML
    public void convertUCosts(MouseEvent mouseEvent) {
        int numActualUCosts = 0;
        HBox uCostsHBox = (HBox) productionPane.getChildren().get(2);
        VBox resVBox;
        Label resLabel;
        int newQuantity;
        PhysicalResource selectedResource = extractSelectedResource((ImageView) mouseEvent.getSource());

        for (PhysicalResource uCost : uCosts)
            numActualUCosts += uCost.getQuantity();

        if(numActualUCosts >= numUCosts){
            PhysicalResource oldestResource = uCosts.get(0);
            newQuantity = oldestResource.getQuantity() - 1;
            if (newQuantity == 0)
                uCosts.remove(0);
            else {
                uCosts.remove(oldestResource);
                oldestResource = new PhysicalResource(oldestResource.getType(), newQuantity);
                uCosts.add(oldestResource);
            }

            resVBox = (VBox) uCostsHBox.getChildren().get(oldestResource.getType().ordinal()-1);
            resLabel = (Label) resVBox.getChildren().get(0);
            resLabel.setText("x" + newQuantity);
        }
        int indexOfResource = uCosts.indexOf(selectedResource);
        if(indexOfResource != -1) {
            PhysicalResource foundResource = uCosts.remove(indexOfResource);
            newQuantity = foundResource.getQuantity() + 1;
            foundResource = new PhysicalResource(foundResource.getType(), newQuantity);
            uCosts.add(foundResource);
        }
        else {
            uCosts.add(selectedResource);
            newQuantity = 1;
        }

        resVBox = (VBox) uCostsHBox.getChildren().get(selectedResource.getType().ordinal()-1);
        resLabel = (Label) resVBox.getChildren().get(0);
        resLabel.setText("x" + newQuantity);


        if(uCosts.size() == 2) {
            unknownCost1.setImage(uCosts.get(0).getType().asImage());
            unknownCost2.setImage(uCosts.get(1).getType().asImage());
        }
        else if(uCosts.size() == 1 && uCosts.get(0).getQuantity() == 2){
            unknownCost1.setImage(uCosts.get(0).getType().asImage());
            unknownCost2.setImage(uCosts.get(0).getType().asImage());
        }
        else if(uCosts.size() == 1)
            unknownCost1.setImage(uCosts.get(0).getType().asImage());

        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));

        mouseEvent.consume();
    }

    /**
     * Increase the count on the label of the clicked resource ({@link MouseEvent#getSource()})
     * and adds the resource to the list of conversions for unknown earnings.
     * It also put on the imageView of the unknown earning of the card the image of the chosen resource.
     * If already exists a conversion for the selected unknown earning it replace it.
     * @param mouseEvent the mouseEvent that calls the method.
     */
    @FXML
    public void convertUEarnings(MouseEvent mouseEvent) {
        int numActualEarnings = 0;
        int newFirstResource = uEarnings.size()-1;
        HBox uCostsHBox = (HBox) productionPane.getChildren().get(4);
        VBox resVBox;
        Label resLabel;
        int newQuantity;

        PhysicalResource selectedResource = extractSelectedResource((ImageView) mouseEvent.getSource());

        for (Resource uEarning : uEarnings)
            numActualEarnings += uEarning.getQuantity();

        if(numUEarnings == numActualEarnings){
            Resource oldestResource = uEarnings.get(newFirstResource);
            newQuantity = oldestResource.getQuantity() - 1;
            uEarnings.remove(newFirstResource);

            resVBox = (VBox) uCostsHBox.getChildren().get(((PhysicalResource) oldestResource).getType().ordinal()-1);
            resLabel = (Label) resVBox.getChildren().get(0);
            resLabel.setText("x" + newQuantity);
        }

        int indexOfResource = uEarnings.indexOf(selectedResource);
        if(indexOfResource != -1) {
            Resource foundResource = uEarnings.remove(indexOfResource);
            newQuantity = foundResource.getQuantity() + 1;
            foundResource = new PhysicalResource(((PhysicalResource) foundResource).getType(), newQuantity);
            uEarnings.add(foundResource);
        }
        else {
            uEarnings.add(selectedResource);
        }



        resVBox = (VBox) uCostsHBox.getChildren().get(selectedResource.getType().ordinal()-1);
        resLabel = (Label) resVBox.getChildren().get(0);
        resLabel.setText("x1");

        if(cardsToProduce.get(cardsToProduce.size()-1).equals("BASICPROD"))
            unknownEarning.setImage(((PhysicalResource) uEarnings.get(uEarnings.size()-1)).getType().asImage());
        else {
            if(getSceneProxy().getCardID(((ImageView) activeLeaders.getChildren().get(0)).getImage()).equals(cardsToProduce.get(cardsToProduce.size()-1)))
                firstProdLeaderUnknown.setImage(((PhysicalResource) uEarnings.get(uEarnings.size()-1)).getType().asImage());
            else
                secondProdLeaderUnknown.setImage(((PhysicalResource) uEarnings.get(uEarnings.size()-1)).getType().asImage());
        }

        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));

        mouseEvent.consume();
    }

    /**
     * Sets the text of each label as "x0" and than sets productionPane as invisible and disabled.
     */
    @FXML
    public void closeProductionPane() {
        HBox hBox = (HBox) productionPane.getChildren().get(2);
        VBox vBox;

        for (Node node : hBox.getChildren()){
            vBox = (VBox) node;
            ((Label) vBox.getChildren().get(0)).setText("x0");
        }

        hBox = (HBox) productionPane.getChildren().get(4);

        for (Node node : hBox.getChildren()){
            vBox = (VBox) node;
            ((Label) vBox.getChildren().get(0)).setText("x0");
        }

        productionPane.setDisable(true);
        productionPane.setVisible(false);
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% DRAG AND DROP %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Starts a Drag and drop sequence for a resource (it is shared by Warehouse, Strongbox and MarketBuffer)
     * @param mouseEvent the mouseEvent that starts the sequence
     */
    @FXML
    public void dragResource(MouseEvent mouseEvent) {
        ImageView resource = (ImageView) mouseEvent.getSource();
        Dragboard db = resource.startDragAndDrop(TransferMode.MOVE);
        temporaryRes = ResType.valueOfImage(resource.getImage());

        ClipboardContent content = new ClipboardContent();
        content.putImage(resource.getImage());
        db.setContent(content);

        //Avoid behind objects to detect this event
        mouseEvent.consume();
    }

    /**
     * If you can drop the resource here({@link DragEvent#getSource()}) sets a temporary message {@link WarehouseInsertionMessage} with the relative list of chosen resources,
     * and set the image of the chosen resource in the selected imageView.
     * It doesn't control if you can place effectively the resource but only if there is still space in the HBox
     * @param dragEvent the event that triggered this method
     */
    @FXML
    public void dropResourceWarehouse(DragEvent dragEvent) {
        boolean success;
        ImageView selectedPlace = (ImageView)dragEvent.getSource();
        Image draggedImage = dragEvent.getDragboard().getImage();
        if(selectedPlace.getImage() == null) {
            success = true;

            selectedPlace.setImage(draggedImage);
            chosenResources.add(0, new PhysicalResource(temporaryRes, searchShelf(selectedPlace)));

            message = new WarehouseInsertionMessage(player.getNickname(), chosenResources);
        }
        else
            success = false;

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }

    /**
     * Controls if the grandParent of the source is the same of the gesture source, if not -> the drop is acceptable
     * @param dragEvent the relative dragEvent
     */
    @FXML
    public void acceptDrop(DragEvent dragEvent) {
        Node gestureSource = (Node)dragEvent.getGestureSource();
        if(!gestureSource.equals(tempDevCard) && !((gestureSource.getParent().getParent()).equals(((Node) dragEvent.getSource()).getParent().getParent())))
            dragEvent.acceptTransferModes(TransferMode.MOVE);

        dragEvent.consume();
    }

    /**
     * End the drag sequence for a sequence started from an ImageView -> remove the image
     * @param dragEvent the relative dragEvent
     */
    @FXML
    public void dragDone(DragEvent dragEvent) {
        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            ((ImageView) dragEvent.getGestureSource()).setImage(null);
        }

        dragEvent.consume();
    }

    /**
     * End the drag sequence for a sequence started from the strongbox
     * @param dragEvent the relative dragEvent
     */
    @FXML
    public void dragStrongBoxResourceDone(DragEvent dragEvent) {
        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            HBox hBox = (HBox) ((Node) dragEvent.getGestureSource()).getParent();
            Text text = (Text) hBox.getChildren().get(1);
            int newNum = Integer.parseInt(text.getText().substring(1)) - 1;
            text.setText("x" + newNum);
        }

        dragEvent.consume();
    }

    /**
     * If you can drop the resource here({@link DragEvent#getSource()}) increments the count on the relative label.
     * Than it modify the list of payments from strongbox or from warehouse depends on the granParent of {@link DragEvent#getGestureSource()}
     * and sets a temporary message {@link PaymentsMessage} with the relative lists of resources to pay from warehouse or strongbox.
     * @param dragEvent the event that triggered this method
     */
    @FXML
    public void dropResourcePayments(DragEvent dragEvent) {
        boolean success;
        int numRes;
        ImageView selectedPlace = (ImageView)dragEvent.getSource();
        if(selectedPlace.getParent().isVisible()) {
            success = true;

            VBox paymentsResources = (VBox) paymentsPane.getChildren().get(2);

            for(Node n : paymentsResources.getChildren()){
                HBox res = (HBox) n;
                if(temporaryRes.toString().toLowerCase().equals(res.getId())){
                    Label resLabel = (Label) res.getChildren().get(1);
                    numRes = Integer.parseInt(resLabel.getText().substring(1))+1;
                    resLabel.setText("x" + numRes);
                }
            }

            if(((Node) dragEvent.getGestureSource()).getParent().getParent().equals(warehousePane)) {
                int numShelf = warehousePane.getChildren().indexOf((((Node) dragEvent.getGestureSource()).getParent()))+1;
                if(paymentsFromWarehouse.containsKey(numShelf))
                    paymentsFromWarehouse.replace(numShelf, new PhysicalResource(temporaryRes, paymentsFromWarehouse.get(numShelf).getQuantity() + 1));
                else
                    paymentsFromWarehouse.put(numShelf, new PhysicalResource(temporaryRes, 1));
            }
            else if(((Node) dragEvent.getGestureSource()).getParent().getParent().equals(strongBox)) {
                PhysicalResource resourceToPay = new PhysicalResource(temporaryRes, 1);
                int indexOfResourceToPay = paymentsFromStrongbox.indexOf(resourceToPay);
                if(indexOfResourceToPay != -1) {
                    int oldQuantity = paymentsFromStrongbox.get(indexOfResourceToPay).getQuantity();
                    paymentsFromStrongbox.remove(resourceToPay);
                    resourceToPay = new PhysicalResource(temporaryRes,  oldQuantity+ 1);
                }

                paymentsFromStrongbox.add(resourceToPay);
            }

            message = new PaymentsMessage(player.getNickname(), paymentsFromStrongbox, paymentsFromWarehouse);

        }
        else
            success = false;

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% UPDATES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Updates the graphic visualization of the hand leaders for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param newHandLeaders the new value
     */
    public void updateHandLeaders(String nickname, List<String> newHandLeaders) {

        if(nickname.equals(player.getNickname())){
            for (int i = 0; i < handLeaders.getChildren().size(); i++) {
                if(i < newHandLeaders.size())
                    ((ImageView) handLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(newHandLeaders.get(i)));
                else
                    ((ImageView) handLeaders.getChildren().get(i)).setImage(null);

                deleteLeaderMenu();
            }

        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId())) {
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if (("handLeaders").equals(child.getId())) {
                            populateHandLeadersImages(newHandLeaders, (HBox) child);
                            return;
                        }
                }
        }
    }

    /**
     * Updates the graphic visualization of the active leaders for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param newActiveLeaders the new value
     */
    public void updateActiveLeaders(String nickname, List<String> newActiveLeaders) {
        if(nickname.equals(player.getNickname())){
            setActiveLeadersImages(newActiveLeaders, activeLeaders);

            //trying to enable extra shelves here instead of enabling it on the activateLeader() method.
            boolean firstSlotActivated = false;
            for(String cardId : newActiveLeaders)
                if(getSceneProxy().isSlotLeader(cardId))
                    if(!firstSlotActivated) {
                        firstSlotActivated = true;
                        SceneProxy.getChildById(warehousePane, "shelf4").setDisable(false);
                    }
                    else {
                        SceneProxy.getChildById(warehousePane, "shelf5").setDisable(false);
                    }

        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId())) {
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if (("activeLeaders").equals(child.getId())) {
                            setActiveLeadersImages(newActiveLeaders, (HBox) child);
                            return;
                        }
                }
        }
    }

    /**
     * Updates the graphic visualization of the market to the new passed value
     * @param market the new value
     */
    public void updateMarket(char[][] market) {
        ImageView imageView;
        for(int i=0; i< 3; i++){
            for (int j = 0; j < 4; j++) {
                imageView = (ImageView) marketGrid.getChildren().get(i*4+j);
                imageView.setImage(getSceneProxy().getMarbleImage(market[i][j]));
            }
        }
        slideMarble.setImage(getSceneProxy().getMarbleImage(match.getSideMarble()));

        for (Node child : marketPane.getChildren()) {
            if(!child.getId().equals(marketGrid.getId()))
                child.setEffect(null);
        }

    }

    /**
     * Updates the graphic visualization of the market buffer for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param marketBuffer the new value
     */
    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {

        if(nickname.equals(player.getNickname()))
            populateMarketBuffer(marketBuffer, marketBufferBox);

        else{
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("marketBuffer").equals(child.getId())) {
                            VBox enemyMarketBuffer = (VBox) child;
                            populateMarketBuffer(marketBuffer, enemyMarketBuffer);
                        }
        }

        message = new WarehouseInsertionMessage(player.getNickname(), List.of(new PhysicalResource(ResType.UNKNOWN, 0)));
    }

    /**
     * Updates the graphic visualization of the warehouse for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param warehouse the new value
     */
    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {
        if(nickname.equals(player.getNickname())){
            for (Node box : warehousePane.getChildren()){
                HBox shelf = (HBox) box;
                shelf.setEffect(null); //remove eventually glow from switch shelf
                for (Node place : shelf.getChildren())
                    ((ImageView) place).setImage(null);
            }
            populateWarehouse(warehouse, warehousePane);
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("warehousePane").equals(child.getId())){
                            for (Node box : ((Pane) child).getChildren()){
                                HBox shelf = (HBox) box;
                                for (Node place : shelf.getChildren())
                                    ((ImageView) place).setImage(null);
                            }
                            populateWarehouse(warehouse, (Pane) child);
                        }
        }

        //if i tried a switch shelf while i had placed other resources i need to return to a consistent visualization
        if(nickname.equals(player.getNickname()))
            updateMarketBuffer(nickname, player.getMarketBuffer());
    }

    /**
     * Updates the graphic visualization of the faith marker for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param faithMarker the new value
     */
    public void updateFaithMarker(String nickname, int faithMarker) {

        if(nickname.equals("Lorenzo the Magnificent"))
            lorenzoMarker.setText(faithMarker + "");

        if(nickname.equals(player.getNickname())){
            for(Node cell : faithPath.getChildren())
                cell.setVisible(false);

            faithPath.getChildren().get(faithMarker).setVisible(true);
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("faithPath").equals(child.getId())){
                            GridPane enemyFaithPath = (GridPane) child;
                            for(Node cell : enemyFaithPath.getChildren())
                                cell.setVisible(false);
                            enemyFaithPath.getChildren().get(faithMarker).setVisible(true);
                        }
        }

    }

    /**
     * Updates the graphic visualization of the popeTiles for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param popeTiles the new value
     */
    public void updatePopeTiles(String nickname, List<Integer> popeTiles) {
        Pane interestedBoard = null;

        //searching the pane
        if(nickname.equals(getClient().getNickname()))
            interestedBoard = myPane;
        else
            for(Node board : enemiesBox.getChildren())
                if(nickname.equals(board.getId()))
                    interestedBoard = (Pane) board;

        populatePopeTiles(popeTiles, interestedBoard);

    }

    /**
     * Updates the graphic visualization of the strongbox for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param newStrongbox the new value
     */
    public void updateStrongBox(String nickname, List<PhysicalResource> newStrongbox) {
        if(nickname.equals(player.getNickname()))
            populateStrongbox(newStrongbox, strongBox);

        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId())) {
                    for (Node child : ((Pane) enemyPane).getChildren()) {
                        if (("strongBox").equals(child.getId())) {
                            VBox sb = (VBox) child;
                            populateStrongbox(newStrongbox, sb);
                        }
                    }
                }
        }
    }

    /**
     * Updates the graphic visualization of the connection state for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param connected true if the player is connected, false if it is disconnected
     */
    public void updateDisconnections(String nickname, boolean connected) {
        String message = null;

        for(Node enemyPane : enemiesBox.getChildren())
            if ((nickname).equals(enemyPane.getId())) {
                Pane chosenEnemy = (Pane) enemyPane;
                for(Node n : chosenEnemy.getChildren())
                    if(("nicknameLabel").equals(n.getId())) {
                        Label label = (Label) n;
                        if (connected) {
                            label.setText(nickname);
                            label.setTextFill(Color.WHITE);
                            message = "player " + nickname + " is back in the game! ";
                        } else {
                            label.setText(nickname + " (OFFLINE)");
                            label.setTextFill(Color.RED);
                            message = "player " + nickname + " has left the match ";
                        }
                        break;
                    }
                break;
            }
        informationField.appendText("\n" + message);

    }

    /**
     * Updates the graphic visualization of the last token drawn and the remaining tokens to the new passed values
     * @param tokenName the name of the extracted token
     * @param remainingTokens the number of tokens remained in the pile
     */
    public void updateTokenDrawn(String tokenName, int remainingTokens) {

        this.remainingTokens.setText("(" + remainingTokens + " remaining)");
        tokenDrawn.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/punchBoard/" + tokenName + ".png"))));
    }

    /**
     * Updates the graphic visualization of the CardGrid to the new passed value
     * @param cards the new value
     */
    public void updateCardGrid(List<String>[][] cards){
        StackPane stackPane;
        List<String> cardAndDepth;
        String card;
        int depth;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                cardAndDepth = cards[2-i][j];
                card = cardAndDepth.get(0);
                depth = Integer.parseInt(cardAndDepth.get(1))-1;

                stackPane = (StackPane) cardGrid.getChildren().get(i*4+j);

                setStackPaneContent(stackPane, depth, card);
            }
        }
    }

    /**
     * Updates the graphic visualization of the TempDevCard to the new passed value or remove the image if the id is not correct or it is null or ""
     * @param card the id of the new tempDevCard or null if there isn't a tempDevCard
     */
    public void updateTempDevCard(String card){
        if(card == null || card.equals("")) {
            tempDevCard.setImage(null);
            return;
        }
        tempDevCard.setImage(getSceneProxy().getCardImage(card));
    }

    /**
     * Updates the graphic visualization of the DevCardSlots for the relative player to the new passed value
     * @param nickname the player whom the update is faced to
     * @param devCardSlots the new value
     */
    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots){
        HBox devCardHBox;
        Pane playerPane;

        if(nickname.equals(player.getNickname()))
            playerPane = myPane;
        else
            playerPane = (Pane) getChildById(enemiesBox, nickname);

        devCardHBox = (HBox)getChildById(playerPane, "devCardSlots");
        populateDevCardSlots(devCardSlots, devCardHBox);

        updateTempDevCard(match.getLightPlayer(nickname).getTempDevCard());
    }

    /**
     * Displays a pop-up showing the relative message
     * @param errMessage the message you want to show
     */
    public void printRetry(String errMessage) {
        JavaFXGUI.popUpWarning(errMessage);
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% LAST ROUND FUNCTIONS  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Setter
     * @param value the new last round value
     */
    public void setLastRound(boolean value) {
        this.lastRound = value;
    }

    /**
     * Puts visible the flag about the last round of the match.
     */
    public void printLastRound() {
        lastRoundFlag.setVisible(true);
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% OTHER FUNCTIONS  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    /**
     * Displays a pop-up showing the playerBoard of the clicked player
     * @param mouseEvent the event that has started this method
     */
    @FXML
    public void zoom(MouseEvent mouseEvent) {
        String nickname = ((Pane) mouseEvent.getSource()).getId();
        JavaFXGUI.popUpZoom(match.getLightPlayer(nickname));

        mouseEvent.consume();
    }

    /**
     * Function called by the press of confirm button, sends the temporary message saved or EndTurnMessage if it is the case
     */
    @FXML
    public void sendMessage() {
        if(endTurn())
            (new EndTurnMessage(player.getNickname())).send();
        else {
            if (message != null) {
                message.send();
                message = null;
            }
        }
        initializeTemporaryVariables();
    }

    /**
     * Controls the state on the {@link it.polimi.ingsw.view.ClientController} and returns true if it is the end of the turn
     * @return true if it is the end of the turn
     */
    private boolean endTurn(){
        return getClientController().getCurrentState().equals(StateName.END_TURN);
    }

}