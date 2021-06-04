package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class TurnSceneController implements SceneController{
    public Pane basePane;
    public GridPane marketGrid;
    public GridPane cardGrid;
    public ImageView slideMarble;
    public Pane myPane;
    public HBox handLeaders;
    public HBox activeLeaders;
    public HBox marketBufferBox;
    public Pane warehousePane;
    public VBox strongBox;
    public Pane marketPane;
    public Pane paymentsPane;
    public GridPane faithPath;
    public VBox enemiesBox;
    public TextField informationsField;
    public Button confirmButton;
    private ResType temporaryRes;
    private LightMatch match;
    private LightPlayer player;
    private Integer firstShelfToSwitch;
    private List<PhysicalResource> chosenResources = new ArrayList<>();
    private List<PhysicalResource> paymentsFromWarehouse = new ArrayList<>();
    private List<PhysicalResource> paymentsFromStrongbox = new ArrayList<>();
    private CtoSMessage message;


    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
        match = getClientController().getMatch();
        player = match.getLightPlayer(getClient().getNickname());

        firstShelfToSwitch = null;
        System.out.println("creating turnController");
    }


    public void loadStartingTurn() {
        ImageView imageView;
        StackPane stackPane;
        List<String>[][] cards;
        List<LightPlayer> enemies = new ArrayList<>(match.getLightPlayers());
        enemies.remove(player);

        int k;
        for (k=0; k < enemies.size(); k++) {
            Pane enemyPane = (Pane) enemiesBox.getChildren().get(k);
            enemyPane.setId(enemies.get(k).getNickname());
            Label enemyName = (Label) enemyPane.getChildren().get(2);
            enemyName.setText(enemies.get(k).getNickname());
        }

        //in order to put invisible the boards of the remaining free players' positions.
        for(int h = k; h<3; h++)
            enemiesBox.getChildren().get(h).setVisible(false);

        for(Node n : cardGrid.getChildren()){
            List<Node> images = ((StackPane) n).getChildren();
            for (int i=0; i < images.size()-1; i++)
                images.get(i).setDisable(true);
        }

        updateMarket(match.getMarket());

        cards = match.getCardGrid();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stackPane = (StackPane) cardGrid.getChildren().get(i*4+j);
                imageView = (ImageView) stackPane.getChildren().get(3);
                imageView.setImage(getSceneProxy().getCardImage(cards[i][j].get(0)));
            }
        }

        updateHandLeaders(player.getNickname(), player.getHandLeaders());
        updateMarketBuffer(player.getNickname(), player.getMarketBuffer());

        for (LightPlayer player1 : match.getLightPlayers()) {
            updateWarehouse(player1.getNickname(), player1.getWarehouse());
            updateFaithMarker(player1.getNickname(), player1.getFaithMarker());
        }
    }

    public void disableAll(boolean value) {
        basePane.setDisable(value);
        if(value) {
            marketPane.setDisable(false);
            cardGrid.setDisable(false);
        }
    }

    public void marketActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>();
        importantIds.addAll(List.of("warehousePane","activeLeaders"));

        keepEnabledOnly(importantIds);

        for(Node n : myPane.getChildren())
            //putting visible the labels for the white marble conversions
            if(("firstConversionCount").equals(n.getId()) || ("secondConversionCount").equals(n.getId()))
                n.setVisible(true);
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


    }

    public void buyDevActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);
        paymentsPane.setDisable(false);
        paymentsPane.setVisible(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "strongbox"));

        keepEnabledOnly(importantIds);

    }

    public void placeDevCardPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "firstDevSlot", "secondDevSlot", "thirdDevSlot"));

        keepEnabledOnly(importantIds);
    }

    public void productionActionPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "firstDevSlot", "secondDevSlot", "thirdDevSlot"));

        keepEnabledOnly(importantIds);

    }

    public void endTurnPhaseDisables(){
        marketPane.setDisable(true);
        cardGrid.setDisable(true);

        //ids of the elements to keep enabled
        List<String> importantIds = new ArrayList<>(List.of("warehousePane", "firstDevSlot", "secondDevSlot", "thirdDevSlot"));

        keepEnabledOnly(importantIds);

    }

    /**
     * This method keeps enabled only the requested elements and disables everything else.
     * @param ids list of chosen elements' ids
     */
    public void keepEnabledOnly(List<String> ids){
        ids.addAll(List.of("exitButton","confirmButton"));
        for(Node n : myPane.getChildren())
            if(!ids.contains(n.getId()))
                n.setDisable(true);
            else
                n.setDisable(false);
    }

    @FXML
    public void clickOnWarehouse(MouseEvent mouseEvent) {
        ImageView clicked = (ImageView) mouseEvent.getSource();
        int shelf = searchShelf(clicked);
        if (shelf > 3 && shelf > player.getWarehouse().size()) {
            firstShelfToSwitch = null;
            return;
        }

        if (firstShelfToSwitch == null) {
            firstShelfToSwitch = shelf;
            return;
        }

        message = new SwitchShelfMessage(player.getNickname(), firstShelfToSwitch, shelf);
        confirmButton.setText("confirm");
        firstShelfToSwitch = null;

    }

    private int searchShelf(ImageView clicked){
        return warehousePane.getChildren().indexOf(clicked.getParent())+1;
    }

    /**
     * Sets the nodes in the scene correctly disabled or not, respect to the current state
     */
    private void returnToCurrentState(){
        //todo: return to current state
    }

    /**
     * Disables all the nodes in the scene minus the one with the given id, the node should be among the direct children
     * of the basePane or myPane, otherwise every node will be disabled.
     * Warehouse pane will never be disabled
     * @param id the id of the node you want to not disable
     */
    public void disableAllMinus(String id){
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


    public void yourTurn(boolean yourTurn) {
        if (yourTurn) {
            informationsField.setText("It's your turn! Make a move");
        }

        else{
            informationsField.setText("Wait your turn");
        }
        confirmButton.setText("confirm");
        disableAll(!yourTurn);
    }

    @FXML
    public void columnCall(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource() ;
        String data = (String) node.getUserData();
        int numColumn = Integer.parseInt(data);

        for (Node child : marketPane.getChildren()) {
            if(!child.getId().equals(marketGrid.getId()))
                child.setEffect(null);
        }

        node.setEffect(new Glow(1));

        message = new MarketDrawMessage(player.getNickname(), false, numColumn);
    }

    @FXML
    public void rowCall(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource() ;
        String data = (String) node.getUserData();
        int numRow = Integer.parseInt(data);

        for (Node child : marketPane.getChildren()) {
            if(!child.getId().equals(marketGrid.getId()))
                child.setEffect(null);
        }

        node.setEffect(new Glow(1));

        message = new MarketDrawMessage(player.getNickname(), true, numRow);
    }

    @FXML
    public void sendMessage() {
        if(getClientController().getCurrentState().equals(StateName.END_TURN) && !message.getType().equals(CtoSMessageType.SWITCH_SHELF) && !message.getType().equals(CtoSMessageType.LEADER_DISCARDING) && !message.getType().equals(CtoSMessageType.LEADER_ACTIVATION) )
            (new EndTurnMessage(player.getNickname())).send();
        else
            if(message != null) {
                message.send();
                message = null;
            }
    }

    public void updateHandLeaders(String nickname, List<String> newHandLeaders) {
        if(nickname.equals(player.getNickname())){
            for (Node child : handLeaders.getChildren())
                ((ImageView) child).setImage(null);
            for (int i=0; i < newHandLeaders.size(); i++) {
                ((ImageView) handLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(newHandLeaders.get(i)));
            }
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("handLeaders").equals(child.getId())){
                            HBox leaders = (HBox) child;
                            if(newHandLeaders.size() < leaders.getChildren().size())
                                for(Node n : leaders.getChildren()) {
                                    ImageView leader = (ImageView) n;
                                    if(leader.getImage() != null) {
                                        leader.setImage(null);
                                        break;
                                    }
                                }
                        }
        }
    }

    public void updateActiveLeaders(String nickname, List<String> newActiveLeaders) {
        if(nickname.equals(player.getNickname())){
            for (Node child : activeLeaders.getChildren())
                ((ImageView) child).setImage(null);
            for (int i=0; i < newActiveLeaders.size(); i++) {
                ((ImageView) activeLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(newActiveLeaders.get(i)));
            }
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("activeLeaders").equals(child.getId())){
                            HBox leaders = (HBox) child;
                            if(newActiveLeaders.size() < leaders.getChildren().size())
                                for(Node n : leaders.getChildren()) {
                                    ImageView leader = (ImageView) n;
                                    if(leader.getImage() != null) {
                                        leader.setImage(null);
                                        break;
                                    }
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

        if(nickname.equals(player.getNickname())) {
            for (Node child :  marketBufferBox.getChildren() )
                ((ImageView) child).setImage(null);
            for (int i = 0; i < marketBuffer.size(); i++)
                ((ImageView) marketBufferBox.getChildren().get(i)).setImage(marketBuffer.get(i).getType().asImage());
        }

        else{
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId()))
                    for (Node child : ((Pane) enemyPane).getChildren())
                        if(("marketBuffer").equals(child.getId())) {
                            VBox enemyMarketBuffer = (VBox) child;
                            for (Node n : enemyMarketBuffer.getChildren())
                                ((ImageView) n).setImage(null);
                            for (int i = 0; i < marketBuffer.size(); i++)
                                ((ImageView) enemyMarketBuffer.getChildren().get(i)).setImage(marketBuffer.get(i).getType().asImage());
                        }
        }

        message = new WarehouseInsertionMessage(player.getNickname(), List.of(new PhysicalResource(ResType.UNKNOWN, 0)));
    }

    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {
        if(nickname.equals(player.getNickname())){
            for (Node box : warehousePane.getChildren()){
                HBox shelf = (HBox) box;
                for (Node place : shelf.getChildren())
                    ((ImageView) place).setImage(null);
            }
            for (int i = 0; i < warehouse.size(); i++) {
                PhysicalResource resShelf = warehouse.get(i);
                HBox shelf = (HBox) warehousePane.getChildren().get(i);
                for (int j = 0; j < resShelf.getQuantity(); j++) {
                    ((ImageView) shelf.getChildren().get(j)).setImage(resShelf.getType().asImage());
                }
            }
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

                            for (int i = 0; i < warehouse.size(); i++) {
                                PhysicalResource resShelf = warehouse.get(i);
                                HBox shelf = (HBox) ((Pane) child).getChildren().get(i);
                                for (int j = 0; j < resShelf.getQuantity(); j++) {
                                    ((ImageView) shelf.getChildren().get(j)).setImage(resShelf.getType().asImage());
                                }
                            }

                        }
        }
    }

    public void updateFaithMarker(String nickname, int faithMarker) {
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

    public void updateStrongBox(String nickname, List<PhysicalResource> newStrongbox) {
        if(nickname.equals(getClient().getNickname())){
            for (int i = 0; i < newStrongbox.size(); i++) {
                for (Node n : strongBox.getChildren()) {
                    HBox shelf = (HBox) n;
                    if (newStrongbox.get(i).getType().toString().equals(shelf.getId()))
                        ((TextField) shelf.getChildren().get(1)).setText("x" + newStrongbox.get(i).getQuantity());
                }
            }
        }
        else {
            for(Node enemyPane : enemiesBox.getChildren())
                if(nickname.equals(enemyPane.getId())) {
                    for (Node child : ((Pane) enemyPane).getChildren()) {
                        if (("strongBox").equals(child.getId())) {
                            VBox sb = (VBox) child;
                            for (int i = 0; i < newStrongbox.size(); i++) {
                                for (Node n : sb.getChildren()) {
                                    HBox shelf = (HBox) n;
                                    if (newStrongbox.get(i).getType().toString().toLowerCase().equals(shelf.getId()))
                                        ((Text) shelf.getChildren().get(1)).setText("x" + newStrongbox.get(i).getQuantity());

                                }
                            }
                        }
                    }
                }
        }
    }

    public void updateDisconnections(String nickname, boolean connected) {
        String str = "";

        for(Node enemyPane : enemiesBox.getChildren())
            if ((nickname).equals(enemyPane.getId())) {
                Pane chosenEnemy = (Pane) enemyPane;
                for(Node n : ((Pane) enemyPane).getChildren())
                    if(("nicknameLabel").equals(n.getId()))
                        if (connected) {
                            //str = "player " + nickname + " is back in the game.";
                            ((Label) n).setText(nickname);
                        } else {
                            //str = "player " + nickname + " has left the match.";
                            ((Label) n).setText(nickname + " (OFFLINE)");
                        }
            }

        informationsField.setText(str+""+informationsField.getText());

    }

    public void printRetry(String errMessage) {
        informationsField.setText(errMessage);
    }

    public void endTurnState() {
        confirmButton.setText("EndTurn");
    }

    @FXML
    public void activateLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());

        message = new LeaderActivationMessage(player.getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()));
        confirmButton.setText("confirm");
    }

    @FXML
    public void discardLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());

        message = new LeaderDiscardingMessage(player.getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()));
        confirmButton.setText("confirm");

    }

    public void drawDevCard(MouseEvent mouseEvent) {

    }
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

    @FXML
    public void acceptDrop(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);

        dragEvent.consume();
    }

    @FXML
    public void dragResourceDone(DragEvent dragEvent) {
        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            ((ImageView) dragEvent.getGestureSource()).setImage(null);
        }

        dragEvent.consume();
    }


    @FXML
    public void dragStrongBoxResourceDone(DragEvent dragEvent) {
        if (dragEvent.getTransferMode() == TransferMode.MOVE) {
            HBox hBox = (HBox) ((Node) dragEvent.getGestureSource()).getParent();
            Text text = (Text) hBox.getChildren().get(1);
            int newNum = Integer.parseInt(text.getText()) - 1;
            text.setText("x" + newNum);
        }

        dragEvent.consume();
    }

    @FXML
    public void dropResourcePayments(DragEvent dragEvent) {
        boolean success;
        int numRes = 0;
        ImageView selectedPlace = (ImageView)dragEvent.getSource();
        Image draggedImage = dragEvent.getDragboard().getImage();
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

            PhysicalResource addedRes = new PhysicalResource(temporaryRes, numRes);
            if(((Node) dragEvent.getGestureSource()).getParent().getParent().equals(warehousePane))
                paymentsFromWarehouse.add(addedRes);
            else if(((Node) dragEvent.getGestureSource()).getParent().getParent().equals(strongBox))
                paymentsFromStrongbox.add(addedRes);

        }
        else
            success = false;

        dragEvent.setDropCompleted(success);
        dragEvent.consume();

    }
}
