package it.polimi.ingsw.view.GUI;

import com.google.gson.stream.JsonToken;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.network.message.ctosmessage.SwitchShelfMessage;
import it.polimi.ingsw.network.message.ctosmessage.WarehouseInsertionMessage;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
    public HBox marketBufferBox;
    public Pane warehousePane;
    private ResType tempResource;
    private LightMatch match;
    private LightPlayer player;
    private Integer firstShelfToSwitch;


    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
        match = getClientController().getMatch();
        player = match.getLightPlayer(getClient().getNickname());
        firstShelfToSwitch = null;
    }


    public void loadStartingTurn() {
        char[][] market = getClientController().getMatch().getMarket();
        ImageView imageView;
        StackPane stackPane;
        List<String>[][] cards;
        List<PhysicalResource> marketBuffer;

        for(int i=0; i< 3; i++){
            for (int j = 0; j < 4; j++) {
                imageView = (ImageView) marketGrid.getChildren().get(i*4+j);
                imageView.setImage(getSceneProxy().getMarbleImage(market[i][j]));
            }
        }
        slideMarble.setImage(getSceneProxy().getMarbleImage(getClientController().getMatch().getSideMarble()));

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

        for (int i=0; i<marketBuffer.size(); i++){
            ((ImageView) marketBufferBox.getChildren().get(i)).setImage(new Image(getClass().getResourceAsStream("images/punchBoard/" + marketBuffer.get(i).getType().toString().toLowerCase() + ".png")));
        }
    }


    @Override
    public void disableAll() {
        basePane.setDisable(true);
    }

    @FXML
    public void resourceSelected(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        int index = marketBufferBox.getChildren().indexOf(imageView);
        if(index > player.getMarketBuffer().size())
            return;
        tempResource = player.getMarketBuffer().get(index).getType();
        System.out.println("set tempResource = "+ tempResource);
       // disableAllMinus("warehousePane");

    }

    @FXML
    public void clickOnWarehouse(MouseEvent mouseEvent) {
        ImageView clicked = (ImageView) mouseEvent.getSource();
        int shelf = searchShelf(clicked);
        System.out.println("clicked on shelf number "+ shelf);
        if (shelf > player.getWarehouse().size()) {
            firstShelfToSwitch = null;
            return;
        }

        if(tempResource != null){
            System.out.println("sending message");
            (new WarehouseInsertionMessage(player.getNickname(), List.of(new PhysicalResource(tempResource, shelf)))).send();
            tempResource = null;
            returnToCurrentState();
            return;
        }

        if (firstShelfToSwitch == null) {
            firstShelfToSwitch = shelf;
           // disableAllMinus("warehousePane");
            return;
        }

        (new SwitchShelfMessage(player.getNickname(), firstShelfToSwitch, shelf)).send();
        firstShelfToSwitch = null;
        returnToCurrentState();

    }

    private int searchShelf(ImageView clicked){
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

        if(!found) {
            myPane.setDisable(false);
            for (Node n : myPane.getChildren()) {
                if (!id.equals(n.getId()))
                    n.setDisable(true);
            }
        }
        warehousePane.setDisable(false);
    }



}
