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
    public Label errorLabel;
    public Label startingResourcesLabel;
    private List<String> leaders = new ArrayList<>();


    public StartingPhaseSceneController() {
        getSceneProxy().setStartingPhaseSceneController(this);
    }


    public void loadLeaderCards(List<String> leaders){
        ImageView leaderImage;

        for (int i = 0; i < 4; i++) {
            leaderImage = (ImageView) leadersHBox.getChildren().get(i);

            leaderImage.setImage(getSceneProxy().getCardImage(leaders.get(i)));
        }
    }

    @FXML
    public void cardSelection(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        String removedLeader;

        if(leaders.size() < 2) {
            if (!leaders.contains(getSceneProxy().getCardID(imageView.getImage())))
                leaders.add(getCardIdAndSelect(imageView));
        }
        else {
            if(leaders.contains(getSceneProxy().getCardID(imageView.getImage())))
                return;
            removedLeader = leaders.remove(0);
            for (Node n : leadersHBox.getChildren()){
                if(getSceneProxy().getCardID(((ImageView) n).getImage()).equals(removedLeader)) {
                    n.setEffect(null);
                    ImageView oldLeader = (ImageView) n;
                    oldLeader.setFitHeight(oldLeader.getFitHeight()-10);
                    oldLeader.setFitWidth(oldLeader.getFitWidth()-10);
                }

            }
            leaders.add(getCardIdAndSelect(imageView));
        }

    }

    private String getCardIdAndSelect(ImageView imageView){
        imageView.setEffect(new Glow(0.3));
        imageView.setFitWidth(imageView.getFitWidth()+10);
        imageView.setFitHeight(imageView.getFitHeight()+10);

        return getSceneProxy().getCardID(imageView.getImage());
    }

    public void sendLeaders() {
        if(leaders.size() != 2) {
            errorLabel.setText("Please chose 2 leaders");
            errorLabel.setOpacity(1);
            return;
        }
        (new LeadersChoiceMessage(getClient().getNickname(), leaders)).send();
    }

    public void loadStartingResources(int numResources){
        startingResources = new ArrayList<>();
        this.numResources = numResources;
        if (numResources == 2)
            startingResourcesLabel.setText("please chose " + numResources + " resources");
        else
            startingResourcesLabel.setText("please chose your resource");

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
        getSceneProxy().loadStartingTurn();
    }

    public void leadersChoiceError(String errorMessage){
        errorLabel.setText(errorMessage);
        errorLabel.setOpacity(1);
    }
    @Override
    public void disableAll() {
        basePane.setDisable(true);
    }
}
