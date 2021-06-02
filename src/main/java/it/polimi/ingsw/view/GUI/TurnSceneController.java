package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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


    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
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

}
