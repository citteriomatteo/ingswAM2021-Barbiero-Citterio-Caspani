package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

import it.polimi.ingsw.network.message.ctosmessage.LeadersChoiceMessage;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.client.Client.getClient;

public class StartingPhaseSceneController implements SceneController{
    private List<ResType> startingResources;
    private int numResources;
    public Pane basePane;
    public HBox resourcesHBox;
    public Label stoneCount;
    public Label shieldCount;
    public Label servantCount;
    public Label coinCount;
    public HBox leadersHBox;
    private String firstLeader;
    private String secondLeader;


    public StartingPhaseSceneController() {
        getSceneProxy().setStartingPhaseSceneController(this);
    }


    public void loadLeaderCards(List<String> leaders){
        ImageView leaderImage;

        for (int i = 0; i < 4; i++) {
            leaderImage = (ImageView) leadersHBox.getChildren().get(i);

            leaderImage.setImage(getSceneProxy().getImage(leaders.get(i)));
        }
    }

    @FXML
    public void cardSelection(MouseEvent mouseEvent) {
        //Migliorabile
        String cardId;
        ImageView imageView = (ImageView) mouseEvent.getSource();
        imageView.setEffect(new Glow(0.3));
        imageView.setFitWidth(imageView.getFitWidth()+10);
        imageView.setFitHeight(imageView.getFitHeight()+10);

        cardId = getSceneProxy().getID(imageView.getImage());

        if(firstLeader == null && secondLeader == null)
            firstLeader = cardId;
        else if(firstLeader != null && secondLeader == null) {
            secondLeader = cardId;
            String nID;
            for(Node n : leadersHBox.getChildren()) {
                nID = getSceneProxy().getID(((ImageView) n).getImage());
                if (!nID.equals(firstLeader) && !nID.equals(secondLeader))
                    n.setDisable(true);
            }
        }
        else if(cardId.equals(firstLeader)){
            firstLeader = null;
            imageView.setEffect(null);
            imageView.setFitHeight(imageView.getFitHeight()-10);
            imageView.setFitWidth(imageView.getFitWidth()-10);
            String nID;
            for(Node n : leadersHBox.getChildren()){
                nID = getSceneProxy().getID(((ImageView) n).getImage());
                if (!nID.equals(secondLeader))
                    n.setDisable(false);
            }
        }
        else {
            secondLeader = null;
            imageView.setEffect(null);
            imageView.setFitHeight(imageView.getFitHeight()-10);
            imageView.setFitWidth(imageView.getFitWidth()-10);
            String nID;
            for(Node n : leadersHBox.getChildren()){
                nID = getSceneProxy().getID(((ImageView) n).getImage());
                if (!nID.equals(firstLeader))
                    n.setDisable(false);
            }
        }

    }

    public void sendLeaders() {
        (new LeadersChoiceMessage(getClient().getNickname(), List.of(firstLeader, secondLeader))).send();
    }

    public void loadStartingResources(int numResources){
        startingResources = new ArrayList<>();
        this.numResources = numResources;
    }

    public void addCoin() {
        ResType coin = ResType.COIN;
        isFinished();

        startingResources.add(coin);
        coinCount.setText("x" + (Integer.parseInt(coinCount.getText().substring(1))+1));
    }

    public void addServant(){
        ResType servant = ResType.SERVANT;
        isFinished();

        startingResources.add(servant);
        servantCount.setText("x" + (Integer.parseInt(servantCount.getText().substring(1))+1));
    }

    public void addShield() {
        ResType shield = ResType.SHIELD;
        isFinished();

        startingResources.add(shield);
        shieldCount.setText("x" + (Integer.parseInt(shieldCount.getText().substring(1))+1));
    }

    public void addStone() {
        ResType stone = ResType.STONE;
        isFinished();

        startingResources.add(stone);
        stoneCount.setText("x" + (Integer.parseInt(stoneCount.getText().substring(1))+1));
    }

    private void isFinished(){
        ResType removed;
        VBox vBox;
        Label label;
        if(startingResources.size() >= numResources) {
            removed = startingResources.remove(0);
            vBox = (VBox) resourcesHBox.getChildren().get(removed.ordinal()-1);
            label = (Label) vBox.getChildren().get(0);
            label.setText("x" + (Integer.parseInt(label.getText().substring(1))-1));
        }
    }

    public void chooseResources() {
        List<PhysicalResource> chosenResources = startingResources.stream()
                .map((x)->new PhysicalResource(x, 1))
                .collect(Collectors.toList());
        getClientController().getMatch().setMarketBuffer(getClient().getNickname(), chosenResources);
        getSceneProxy().changeScene(SceneName.GameScene);
    }

    @Override
    public void disableAll() {
        basePane.setDisable(true);
    }
}
