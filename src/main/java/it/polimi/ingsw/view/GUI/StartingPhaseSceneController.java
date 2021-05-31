package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.essentials.ResType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Map;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

import it.polimi.ingsw.network.message.ctosmessage.LeadersChoiceMessage;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;

import static it.polimi.ingsw.network.client.Client.getClient;

public class StartingPhaseSceneController {
    private Map<ResType, Integer> resourceCounts;
    public Label servantCount;
    public HBox leadersHBox;
    private String firstLeader;
    private String secondLeader;


    public StartingPhaseSceneController() {
        getSceneProxy().setStartingPhaseSceneController(this);
    }

    public void addServant() {
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
        String cardId;
        ImageView imageView = (ImageView) mouseEvent.getSource();

        cardId = getSceneProxy().getID(imageView.getImage());

        if(firstLeader != null && secondLeader == null)
            secondLeader = cardId;
        else firstLeader = cardId;

        System.out.println("setted cardId " + cardId);
    }

    public void sendLeaders() {
        (new LeadersChoiceMessage(getClient().getNickname(), List.of(firstLeader, secondLeader))).send();
    }
}
