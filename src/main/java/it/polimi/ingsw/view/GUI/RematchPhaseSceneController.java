package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.RematchMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

/**
 * A scene controller used to communicate with the EndGame scene
 */
public class RematchPhaseSceneController{
    
    public Pane basePane;
    public Button rematchButton;
    public Button exitButton;
    public Label rematchLabel;
    public VBox rankingBox;

    /**
     * Creates an instance and links it to the {@link SceneProxy}.
     */
    public RematchPhaseSceneController(){
        getSceneProxy().setRematchPhaseSceneController(this);
    }

    /**
     * Populate the ranking table with the players sorted by their points
     * @param ranking a map that links every player to his score
     */
    public void printMatchResults(Map<String, Integer> ranking) {
        int count = 0;

        for(String nickname : getOrderedMapOf(ranking).keySet()) {
            HBox playerLine = (HBox) rankingBox.getChildren().get(count);
            ((Label) playerLine.getChildren().get(1)).setText(nickname);
            ((Label) playerLine.getChildren().get(2))
                    .setText(nickname.equals("Lorenzo \nthe Magnificent") ? "" : ranking.get(nickname).toString());
            count++;
        }

        for(int i = count; i < 4; i++)
            rankingBox.getChildren().get(i).setVisible(false);

    }

    /**
     * Shows the name of the player who has offered a rematch
     * @param nickname the name of the player who has offered a rematch
     */
    public void printRematchOffer(String nickname) {
        rematchLabel.setText(nickname + " has offered a rematch. Want to play again?");
    }

    /**
     * Sends a RematchMessage with true value and sets disabled the buttons on the scene
     */
    @FXML
    public void rematchClicked() {

        rematchButton.setDisable(true);
        exitButton.setDisable(true);

        new RematchMessage(getClientController().getNickname(),true).send();

        rematchLabel.setText("Wait for other players to accept or decline...");
    }

    /**
     * Sends a RematchMessage with false value and sets disabled the buttons on the scene
     */
    @FXML
    public void exitClicked() {
        rematchButton.setDisable(true);
        exitButton.setDisable(true);

        new RematchMessage(getClientController().getNickname(),false).send();

        rematchLabel.setText("You declined. Thanks for playing!");
    }

//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& UTILITY METHODS &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * Orders a map passed on the basis of his Integer values, the LinkedHashMap returned preserve this order
     * @param ranking a map that links String to Integer
     * @return a LinkedHashMap ordered by the Integer score value
     */
    private LinkedHashMap<String, Integer> getOrderedMapOf(Map<String, Integer> ranking) {

        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> sortedRanking = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        ranking.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedRanking.put(x.getKey(), x.getValue()));

        return sortedRanking;

    }


}
