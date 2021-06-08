package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.Resource;
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
import javafx.scene.text.Text;

import java.util.*;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getChildById;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class TurnSceneController implements SceneController{
    public Pane basePane;
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
    public TextField informationField;
    public Button confirmButton;
    public ImageView tempDevCard;
    public HBox devCardSlots;
    public ImageView basicProduction;
    public ImageView unknownCost1;
    public ImageView unknownCost2;
    public ImageView unknownEarning;
    public ImageView firstProdLeaderUnknown;
    public ImageView secondProdLeaderUnknown;

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
    int numUCosts;
    int numUEarnings;
    private ImageView selectedCard;

    private CtoSMessage message;

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% CONSTRUCTOR %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
        match = getClientController().getMatch();
        player = match.getLightPlayer(getClient().getNickname());

        initializeTemporaryVariables();
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%% UTILITY FUNCTIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    private void initializeTemporaryVariables(){
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
    }

    /**
     * Returns the ordinal number of the shelf which contains the 'clicked' Imageview
     * @param clicked the place you want to obtain the related shelf
     * @return the ordinal number of the shelf which contains the 'clicked' Imageview or 0 if it is not part of a shelf
     */
    private int searchShelf(ImageView clicked){
        return warehousePane.getChildren().indexOf(clicked.getParent())+1;
    }

    static public void clearHandLeadersImages(List<String> newHandLeaders, HBox handLeaders) {
        if(newHandLeaders.size() < handLeaders.getChildren().size())
            for(Node n : handLeaders.getChildren()) {
                ImageView leader = (ImageView) n;
                if(leader.getImage() != null) {
                    leader.setImage(null);
                    return;
                }
            }
    }

    static public void setActiveLeadersImages(List<String> newActiveLeaders, HBox activeLeaders) {
        if(newActiveLeaders.size()==2 && getSceneProxy().isSlotLeader(newActiveLeaders.get(1)))
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

    static public void populateMarketBuffer(List<PhysicalResource> marketBuffer, Pane marketBufferBox) {
        List<Node> places = marketBufferBox.getChildren();
        for (int i = 0; i < places.size(); i++) {
            if(i < marketBuffer.size())
                ((ImageView) places.get(i)).setImage(marketBuffer.get(i).getType().asImage());
            else
                ((ImageView) places.get(i)).setImage(null);
        }
    }

    static public void populateDevCardSlots(List<String>[] devCardSlots, HBox devCardHBox) {
        List<String> cardColumn;
        ImageView imageview;
        StackPane cardColumnStack;
        for (int i = 0; i < devCardSlots.length; i++) {
            cardColumn = devCardSlots[i];
            cardColumnStack = (StackPane) devCardHBox.getChildren().get(i);
            for (int j = 0; j < cardColumn.size(); j++) {
                imageview = (ImageView) cardColumnStack.getChildren().get(j);
                imageview.setImage(getSceneProxy().getCardImage(cardColumn.get(j)));
            }
        }
    }

    static public void populateWarehouse(List<PhysicalResource> warehouse, Pane warehousePane) {
        for (int i = 0; i < warehouse.size(); i++) {
            PhysicalResource resShelf = warehouse.get(i);
            HBox shelf = (HBox) warehousePane.getChildren().get(i);
            for (int j = 0; j < resShelf.getQuantity(); j++) {
                ((ImageView) shelf.getChildren().get(j)).setImage(resShelf.getType().asImage());
            }
        }
    }

    static public void populateStrongbox(List<PhysicalResource> newStrongbox, VBox strongboxVbox) {
        for (PhysicalResource resource : newStrongbox) {
            for (Node n : strongboxVbox.getChildren()) {
                HBox shelf = (HBox) n;
                if (resource.getType().toString().toLowerCase().equals(shelf.getId()))
                    ((Text) shelf.getChildren().get(1)).setText("x" + resource.getQuantity());
            }
        }
    }

    static public void populatePopeTiles(List<Integer> popeTiles, Pane interestedPane) {
        //changing the tiles on the specified pane
        for(int i = 0; i < popeTiles.size(); i++){
            String imagePath = "";
            if(popeTiles.get(i) == 1) //tile is upside
                imagePath = "images/punchBoard/upsidePopeTile"+(i+1)+".png";
            if(popeTiles.get(i) == 2) //tile is downside
                imagePath = "images/punchBoard/popeTile"+(i+1)+".png";
            Image tile = new Image(TurnSceneController.class.getResourceAsStream(imagePath));

            for(Node n : interestedPane.getChildren())
                if(("popeTile"+(i+1)).equals(n.getId()))
                    ((ImageView) n).setImage(tile);
        }
    }

    /**
     * Sets the text on the button Confirm to EndTurn
     */
    public void endTurnState() {
        confirmButton.setText("End turn");
    }

    public void resetPayments() {
        VBox resourcesVBox = (VBox) paymentsPane.getChildren().get(1);
        for(Node n : resourcesVBox.getChildren()){
            HBox resourceHBox = (HBox) n;
            Label resourceLabel = (Label) resourceHBox.getChildren().get(1);
            resourceLabel.setText("x0");
        }
        updateWarehouse(player.getNickname(), player.getWarehouse());
        updateStrongBox(player.getNickname(), player.getStrongbox());
    }

//%%%%%%%%%%%%%%%%%%%%%% ONE TIME FUNCTION %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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
     * This method keeps enabled only the requested elements inside the player pane and disables everything else inside the pane.
     * Confirm button always remain activated
     * @param ids list of chosen elements' ids
     */
    public void keepEnabledOnly(List<String> ids){
        ids.add("confirmButton");
        for(Node n : myPane.getChildren())
            n.setDisable(!ids.contains(n.getId()));
    }

    /**
     * Disables all the nodes in the scene minus the one with the given id, the node should be among the direct children
     * of the basePane or myPane, otherwise every node will be disabled.
     * Warehouse pane will never be disabled
     * @param id the id of the node you want to not disable
     */
    public void disableAllMinus(String id){
        //todo: doesn't work properly
        boolean found = false;
        for(Node n : basePane.getChildren())
            if(!id.equals(n.getId())) {
                n.setDisable(true);
                found = true;
            }
            else
                n.setDisable(false);

        if(!found) {
            myPane.setDisable(false);
            for (Node n : myPane.getChildren()) {
                if (!id.equals(n.getId()))
                    n.setDisable(true);
            }
        }
        warehousePane.setDisable(false);
    }

    public void marketActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "activeLeaders"));

        keepEnabledOnly(importantIds);

        for(Node n : myPane.getChildren())
            //putting visible the labels for the white marble conversions
            if(("firstConversionCount").equals(n.getId()) || ("secondConversionCount").equals(n.getId()))
                n.setVisible(true);

        //information field text setting for this state
        informationField.setText("Convert each white marble.");
    }

    public void resourcesPlacementPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "marketBufferBox"));

        keepEnabledOnly(importantIds);

        for(Node n : myPane.getChildren())
            //putting back invisible the labels for the white marble conversions
            if(("firstConversionCount").equals(n.getId()) || ("secondConversionCount").equals(n.getId()))
                n.setVisible(false);

        //information field text setting for this state
        informationField.setText("Place the resources you've got.");
    }

    public void buyDevActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        paymentsPane.setDisable(false);
        paymentsPane.setVisible(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "strongbox"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.setText("Pay the card you've chosen.");
    }

    public void placeDevCardPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
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
        informationField.setText("Place your Development card.");
    }

    public void productionActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        paymentsPane.setDisable(false);
        paymentsPane.setVisible(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "strongbox", "firstDevSlot", "secondDevSlot", "thirdDevSlot"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.setText("Produce.");

    }

    public void endTurnPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        endPaymentsPhase();
        endProductionPhase();

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "firstLeaderActions", "secondLeaderActions"));

        keepEnabledOnly(importantIds);

        //information field text setting for this state
        informationField.setText("It's the end of your turn. Switch shelves, discard/activate leaders or finish the turn.");

    }

    private void endPaymentsPhase() {
        VBox resourcesVBox = (VBox) paymentsPane.getChildren().get(1);
        for(Node n : resourcesVBox.getChildren()){
            HBox resourceHBox = (HBox) n;
            Label resourceLabel = (Label) resourceHBox.getChildren().get(1);
            resourceLabel.setText("x0");
        }
        paymentsPane.setDisable(true);
        paymentsPane.setVisible(false);
    }
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



//%%%%%%%%%%%%%%%%%%%%%%%%%  START / END TURN FUNCTIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    public void yourTurn(boolean yourTurn) {
        if (yourTurn)
            informationField.setText("It's your turn! Make a move.");
        else
            informationField.setText("Wait for your turn.");

        confirmButton.setText("Confirm");
        disableAll(!yourTurn);
        //todo: when the turn starts i can also activate a production
    }

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

    @FXML
    public void activateLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());

        (new LeaderActivationMessage(player.getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()))).send();

        actionEvent.consume();
    }

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
        if(player.getHandLeaders().size() == 0){
            leaderActions1.setDisable(true);
            leaderActions1.setVisible(false);
        }
        else if(player.getHandLeaders().size() == 1){
            leaderActions2.setDisable(true);
            leaderActions2.setVisible(false);
        }

        actionEvent.consume();
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%% MARKET DRAW %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @FXML
    public void columnCall(MouseEvent mouseEvent) {
        int numColumn = marketDraw(mouseEvent);

        message = new MarketDrawMessage(player.getNickname(), false, numColumn);
        mouseEvent.consume();
    }

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

    @FXML
    public void rowCall(MouseEvent mouseEvent) {
        int numRow = marketDraw(mouseEvent);

        message = new MarketDrawMessage(player.getNickname(), true, numRow);
        mouseEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%% DEV CARD BUY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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
            card.setFitWidth(card.getFitWidth() + 5);
            card.setFitHeight(card.getFitHeight() + 5);
        }

        int rowIndex, columnIndex;
        rowIndex = GridPane.getRowIndex(card.getParent());
        columnIndex = GridPane.getColumnIndex(card.getParent());

        message = new DevCardDrawMessage(player.getNickname(), cardGrid.getRowCount()-rowIndex, columnIndex + 1);
        mouseEvent.consume();

    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% DEV CARD PLACEMENT %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @FXML
    public void dragCard(MouseEvent mouseEvent) {
        Dragboard db = tempDevCard.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putImage((new Image(getClass().getResourceAsStream("images/handCard.png"))));
        db.setContent(content);

        //Avoid behind objects to detect this event
        mouseEvent.consume();
    }

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

    @FXML
    public void acceptCardDrop(DragEvent dragEvent) {
        Node gestureSource = (Node)dragEvent.getGestureSource();
        if(tempDevCard.equals(gestureSource))
            dragEvent.acceptTransferModes(TransferMode.MOVE);

        dragEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%% PRODUCTION %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


    @FXML
    public void produce(MouseEvent mouseEvent) {
        ImageView imageViewCard = (ImageView) mouseEvent.getSource();
        System.out.println(imageViewCard.getImage());
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
                if(numCard < 12) {
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
                int numInArray = cardsToProduce.indexOf(selectedCard);
                cardsToProduce.remove(selectedCard);
                uEarnings.remove(numInArray);

                if(getSceneProxy().getCardID(((ImageView) activeLeaders.getChildren().get(0)).getImage()).equals(selectedCard))
                    firstProdLeaderUnknown.setImage(null);
                else
                    secondProdLeaderUnknown.setImage(null);
            }

        }

        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));

        mouseEvent.consume();
    }

    public void produceBasicProd() {
        numUCosts += 2;
        numUEarnings += 1;
        int index = cardsToProduce.indexOf("BASICPROD");

        if(index != -1) {
            int numUofLeaders = 0;
            cardsToProduce.remove("BASICPROD");
            basicProduction.setEffect(null);
            unknownCost1.setImage(null);
            unknownCost2.setImage(null);
            unknownEarning.setImage(null);
            uCosts = new ArrayList<>();
            for (int i = 0; i < index; i++) {
                if(cardsToProduce.get(i).startsWith("L"))
                    numUofLeaders += 1;
            }
            uEarnings.remove(numUofLeaders);
        }
        else {
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

    public void convertUCosts(MouseEvent mouseEvent) {
        int numActualUCosts = 0;
        HBox uCostsHBox = (HBox) productionPane.getChildren().get(1);
        VBox resVBox;
        Label resLabel;
        int newQuantity;
        ImageView selectedResourceImageView = ((ImageView) mouseEvent.getSource());
        ResType selectedType = ResType.UNKNOWN;
        for (ResType type : ResType.values())
            if(type.toString().toLowerCase().equals(selectedResourceImageView.getId())) {
                selectedType = type;
                break;
            }

        PhysicalResource selectedResource = new PhysicalResource(selectedType, 1);

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
        else if(uCosts.size() == 1)
            unknownCost1.setImage(uCosts.get(0).getType().asImage());

        message = new ProductionMessage(player.getNickname(), cardsToProduce, new Production(uCosts, uEarnings));

        mouseEvent.consume();
    }

    public void convertUEarnings(MouseEvent mouseEvent) {
        int numActualEarnings = 0;
        HBox uCostsHBox = (HBox) productionPane.getChildren().get(3);
        VBox resVBox;
        Label resLabel;
        int newQuantity;
        ImageView selectedResourceImageView = ((ImageView) mouseEvent.getSource());
        ResType selectedType = ResType.UNKNOWN;

        for (ResType type : ResType.values())
            if(type.toString().toLowerCase().equals(selectedResourceImageView.getId())) {
                selectedType = type;
                break;
            }

        PhysicalResource selectedResource = new PhysicalResource(selectedType, 1);

        for (Resource uCost : uEarnings)
            numActualEarnings += uCost.getQuantity();

        if(numActualEarnings >= numUEarnings){
            Resource oldestResource = uEarnings.get(0);
            newQuantity = oldestResource.getQuantity() - 1;
            if (newQuantity == 0)
                uEarnings.remove(0);
            else
                oldestResource = new PhysicalResource(((PhysicalResource) oldestResource).getType(), newQuantity);

            resVBox = (VBox) uCostsHBox.getChildren().get(((PhysicalResource) oldestResource).getType().ordinal()-1);
            resLabel = (Label) resVBox.getChildren().get(0);
            resLabel.setText("x" + newQuantity);
        }

        int indexOfResource = uEarnings.indexOf(selectedResource);
        if(indexOfResource != -1) {
            Resource foundResource = uEarnings.get(indexOfResource);
            newQuantity = foundResource.getQuantity() + 1;
            foundResource = new PhysicalResource(((PhysicalResource) foundResource).getType(), newQuantity);
        }
        else {
            uEarnings.add(selectedResource);
            newQuantity = 1;
        }

        resVBox = (VBox) uCostsHBox.getChildren().get(selectedResource.getType().ordinal()-1);
        resLabel = (Label) resVBox.getChildren().get(0);
        resLabel.setText("x" + newQuantity);

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

    public void closeProductionPane() {
        HBox hBox = (HBox) productionPane.getChildren().get(1);
        VBox vBox;

        for (Node node : hBox.getChildren()){
            vBox = (VBox) node;
            ((Label) vBox.getChildren().get(0)).setText("x0");
        }

        hBox = (HBox) productionPane.getChildren().get(3);

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

    @FXML
    public void dropResourceWarehouse(DragEvent dragEvent) {
        boolean success;
        ImageView selectedPlace = (ImageView)dragEvent.getSource();
        Image draggedImage = dragEvent.getDragboard().getImage();
        if(selectedPlace.getImage() == null) {
            success = true;

            selectedPlace.setImage(draggedImage);
            chosenResources.add(new PhysicalResource(temporaryRes, searchShelf(selectedPlace)));

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

    @FXML
    public void dropResourcePayments(DragEvent dragEvent) {
        boolean success;
        int numRes = 0;
        ImageView selectedPlace = (ImageView)dragEvent.getSource();
        if(selectedPlace.getParent().isVisible()) {
            success = true;

            VBox paymentsResources = (VBox) paymentsPane.getChildren().get(1);

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
                PhysicalResource resourceToPay = new PhysicalResource(temporaryRes, numRes);
                int indexOfResourceToPay = paymentsFromStrongbox.indexOf(resourceToPay);
                if(indexOfResourceToPay == -1)
                    paymentsFromStrongbox.add(resourceToPay);
                else {
                    paymentsFromStrongbox.remove(resourceToPay);
                    paymentsFromStrongbox.add(resourceToPay);
                }
            }

            message = new PaymentsMessage(player.getNickname(), paymentsFromStrongbox, paymentsFromWarehouse);

        }
        else
            success = false;

        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }


//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% UPDATES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public void updateHandLeaders(String nickname, List<String> newHandLeaders) {
        if(nickname.equals(player.getNickname())){
            for (int i = 0; i < handLeaders.getChildren().size(); i++) {
                if(i < newHandLeaders.size())
                    ((ImageView) handLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(newHandLeaders.get(i)));
                else
                    ((ImageView) handLeaders.getChildren().get(i)).setImage(null);
            }
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId())) {
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if (("handLeaders").equals(child.getId())) {
                            clearHandLeadersImages(newHandLeaders, (HBox) child);
                            return;
                        }
                }
        }
    }

    public void updateActiveLeaders(String nickname, List<String> newActiveLeaders) {
        if(nickname.equals(player.getNickname())){
            setActiveLeadersImages(newActiveLeaders, activeLeaders);
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

    public void updateDisconnections(String nickname, boolean connected) {
        String message = null;

        for(Node enemyPane : enemiesBox.getChildren())
            if ((nickname).equals(enemyPane.getId())) {
                Pane chosenEnemy = (Pane) enemyPane;
                for(Node n : chosenEnemy.getChildren())
                    if(("nicknameLabel").equals(n.getId()))
                        if (connected) {
                            ((Label) n).setText(nickname);
                            message = "player " + nickname + " is back in the game! ";
                        }
                        else {
                            ((Label) n).setText(nickname + " (OFFLINE)");
                            message = "player " + nickname + " has left the match ";
                        }
            }

        informationField.setText(message + informationField.getText());

    }

    public void updateTokenDrawn(String tokenName, int remainingTokens) {

        this.remainingTokens.setText("(" + remainingTokens + " remaining)");
        tokenDrawn.setImage(new Image(getClass().getResourceAsStream("images/punchBoard/"+tokenName+".png")));
    }

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

    private void setStackPaneContent(StackPane stackPane, int newDepth, String topCard) {
        ImageView imageView;
        List<Node> cards = stackPane.getChildren();
        for (int k = cards.size()-1; k >= 0; k--) {
            imageView = (ImageView) cards.get(k);
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

    public void updateTempDevCard(String card){
        if(card == null || card.equals("")) {
            tempDevCard.setImage(null);
            tempDevCard.setVisible(false);
            return;
        }
        tempDevCard.setImage(getSceneProxy().getCardImage(card));
        tempDevCard.setVisible(true);
    }

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

    private boolean endTurn(){
        return getClientController().getCurrentState().equals(StateName.END_TURN);
    }

}
