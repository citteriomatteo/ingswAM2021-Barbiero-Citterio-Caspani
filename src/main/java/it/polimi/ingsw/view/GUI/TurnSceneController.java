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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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
    public Pane marketPane;
    public VBox enemiesBox;
    public TextField informationsField;
    public Button confirmButton;
    private ResType tempResource;
    private LightMatch match;
    private LightPlayer player;
    private Integer firstShelfToSwitch;
    private List<PhysicalResource> chosenResources = new ArrayList<>();
    private CtoSMessage message;


    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
        match = getClientController().getMatch();
        player = match.getLightPlayer(getClient().getNickname());
        firstShelfToSwitch = null;
        //loadStartingTurn();
    }


    public void loadStartingTurn() {
        char[][] market = getClientController().getMatch().getMarket();
        ImageView imageView;
        StackPane stackPane;
        List<String>[][] cards;
        List<PhysicalResource> marketBuffer;
        List<LightPlayer> enemies = new ArrayList<>(getClientController().getMatch().getLightPlayers());
        enemies.remove(getClientController().getMatch().getLightPlayer(getClient().getNickname()));

        for (int i=0; i < enemies.size(); i++) {
            Pane enemyPane = (Pane) enemiesBox.getChildren().get(i);
            enemyPane.setId(enemies.get(i).getNickname());
            Label enemyName = (Label) enemyPane.getChildren().get(2);
            enemyName.setText(enemies.get(i).getNickname());
        }

        updateMarket(market);

        cards = getClientController().getMatch().getCardGrid();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stackPane = (StackPane) cardGrid.getChildren().get(i*4+j);
                imageView = (ImageView) stackPane.getChildren().get(3);
                imageView.setImage(getSceneProxy().getCardImage(cards[i][j].get(0)));
            }
        }

        for (int i = 0; i < handLeaders.getChildren().size(); i++) {
            ((ImageView) handLeaders.getChildren().get(i)).setImage(getSceneProxy().getCardImage(getClientController().getMatch().getLightPlayer(getClient().getNickname()).getHandLeaders().get(i)));
        }

        marketBuffer = getClientController().getMatch().getLightPlayer(getClient().getNickname()).getMarketBuffer();

        updateMarketBuffer(getClient().getNickname(), marketBuffer);

        for (LightPlayer player1 : getClientController().getMatch().getLightPlayers())
            updateWarehouse(player1.getNickname(), player1.getWarehouse());
    }


    @Override
    public void disableAll() {
        basePane.setDisable(true);
    }

    @FXML
    public void resourceSelected(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        int index = marketBufferBox.getChildren().indexOf(imageView);
        if(index > player.getMarketBuffer().size()-1)
            return;
        for (Node child : marketBufferBox.getChildren())
            child.setEffect(null);
        imageView.setEffect(new Glow(0.5));
        tempResource = player.getMarketBuffer().get(index).getType();
        System.out.println("set tempResource = "+ tempResource);
       // disableAllMinus("warehousePane");

    }

    @FXML
    public void clickOnWarehouse(MouseEvent mouseEvent) {
        ImageView clicked = (ImageView) mouseEvent.getSource();
        int shelf = searchShelf(clicked);
        System.out.println("clicked on shelf number "+ shelf);
        if (shelf > 3 && shelf > player.getWarehouse().size()) {
            firstShelfToSwitch = null;
            return;
        }

        if(tempResource != null){
            System.out.println("sending message");
            chosenResources.add(new PhysicalResource(tempResource, shelf));
            for (Node place : ((HBox) warehousePane.getChildren().get(shelf-1)).getChildren()) {
                ImageView res = (ImageView) place;
                if (res.getImage() == null)
                    res.setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + tempResource.toString().toLowerCase() + ".png")));
                break;
            }

            message = new WarehouseInsertionMessage(player.getNickname(), chosenResources);
            //(new WarehouseInsertionMessage(player.getNickname(), List.of(new PhysicalResource(tempResource, shelf)))).send();
            tempResource = null;
            for (Node child : marketBufferBox.getChildren())
                child.setEffect(null);
            //returnToCurrentState();
            return;
        }

        if (firstShelfToSwitch == null) {
            firstShelfToSwitch = shelf;
           // disableAllMinus("warehousePane");
            return;
        }

        message = new SwitchShelfMessage(player.getNickname(), firstShelfToSwitch, shelf);
        firstShelfToSwitch = null;
        //returnToCurrentState();

    }

    private int searchShelf(ImageView clicked){
        System.out.println(clicked);
        List<Node> shelves = warehousePane.getChildren();
        for (int i = 0; i < shelves.size(); i++) {
            for(Node n : ((HBox)shelves.get(i)).getChildren())
                if(n.equals(clicked))
                    return i+1;
        }
        return -1;
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
            basePane.setDisable(false);
        }

        else {
            informationsField.setText("Wait your turn");
            disableAll();
        }
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

        message = new MarketDrawMessage(getClient().getNickname(), false, numColumn);
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

        message = new MarketDrawMessage(getClient().getNickname(), true, numRow);
    }

    @FXML
    public void sendMessage() {
        if(getClientController().getCurrentState().equals(StateName.END_TURN))
            (new EndTurnMessage(getClient().getNickname())).send();
        else
            if(message != null)
                message.send();
    }

    public void updateHandLeaders(String nickname, List<String> newHandLeaders) {
        if(nickname.equals(getClient().getNickname())){
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
        if(nickname.equals(getClient().getNickname())){
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
        slideMarble.setImage(getSceneProxy().getMarbleImage(getClientController().getMatch().getSideMarble()));

        for (Node child : marketPane.getChildren()) {
            if(!child.getId().equals(marketGrid.getId()))
                child.setEffect(null);
        }

    }

    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {
        if(nickname.equals(getClient().getNickname())) {
            for (Node child :  marketBufferBox.getChildren() )
                ((ImageView) child).setImage(null);
            for (int i = 0; i < marketBuffer.size(); i++)
                ((ImageView) marketBufferBox.getChildren().get(i)).setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + marketBuffer.get(i).getType().toString().toLowerCase() + ".png")));
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
                                ((ImageView) enemyMarketBuffer.getChildren().get(i)).setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + marketBuffer.get(i).getType().toString().toLowerCase() + ".png")));
                        }
        }
    }

    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {
        if(nickname.equals(getClient().getNickname())){
            for (Node box : warehousePane.getChildren()){
                HBox shelf = (HBox) box;
                for (Node place : shelf.getChildren())
                    ((ImageView) place).setImage(null);
            }
            for (int i = 0; i < warehouse.size(); i++) {
                PhysicalResource resShelf = warehouse.get(i);
                HBox shelf = (HBox) warehousePane.getChildren().get(i);
                for (int j = 0; j < resShelf.getQuantity(); j++) {
                    ((ImageView) shelf.getChildren().get(j)).setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + resShelf.getType().toString().toLowerCase() + ".png")));
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
                                    ((ImageView) shelf.getChildren().get(j)).setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + resShelf.getType().toString().toLowerCase() + ".png")));
                                }
                            }

                        }
        }
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

        message = new LeaderActivationMessage(getClient().getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()));
    }

    public void discardLeader(ActionEvent actionEvent) {
        MenuItem node = (MenuItem) actionEvent.getSource();
        int numLeader = Integer.parseInt((String) node.getUserData());

        message = new LeaderDiscardingMessage(getClient().getNickname(), getSceneProxy().getCardID(((ImageView) handLeaders.getChildren().get(numLeader-1)).getImage()));

    }

}
