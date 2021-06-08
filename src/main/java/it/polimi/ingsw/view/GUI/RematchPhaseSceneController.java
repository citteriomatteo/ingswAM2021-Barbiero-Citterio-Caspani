package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.RematchMessage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Map;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class RematchPhaseSceneController implements SceneController{
    
    public Pane basePane;
    public Button rematchButton;
    public Button exitButton;
    public Label rematchLabel;
    public VBox rankingBox;

    private boolean rematchOffered = false;

    public RematchPhaseSceneController(){
        getSceneProxy().setRematchPhaseSceneController(this);
    }


    public void printMatchResults(Map<String, Integer> ranking) {

        int count = 0;
        for(String nickname : ranking.keySet()) {
            HBox playerLine = (HBox) rankingBox.getChildren().get(count);
            ((Label) playerLine.getChildren().get(1)).setText(nickname);
            ((Label) playerLine.getChildren().get(2)).setText(ranking.get(nickname).toString());
            count++;
        }

        for(int i = count; i < 4; i++)
            rankingBox.getChildren().get(i).setVisible(false);

    }

    public void printRematchOffer(String nickname) {
        rematchOffered = true;
        rematchLabel.setText(nickname + "has offered a rematch. Want to play again?");
    }

    public void rematchClicked() {

        rematchButton.setDisable(true);
        exitButton.setDisable(true);

        new RematchMessage(getClient().getNickname(),true);

        rematchLabel.setText("Wait for other players to accept or decline...");
    }

    public void exitClicked() {
        rematchButton.setDisable(true);
        exitButton.setDisable(true);

        new RematchMessage(getClient().getNickname(),false);

        rematchLabel.setText("You declined. Thanks for playing!");
    }
}
