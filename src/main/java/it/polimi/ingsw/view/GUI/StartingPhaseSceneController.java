package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
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

/**
 * A scene controller used for communicating with the scenes of the starting phase
 */
public class StartingPhaseSceneController{
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
    private final List<String> leaders;

    /**
     * Creates an instance and links it to the {@link SceneProxy}.
     * Initialize the list for the starting leaders choice.
     */
    public StartingPhaseSceneController() {
        getSceneProxy().setStartingPhaseSceneController(this);
        leaders = new ArrayList<>();
    }

    /**
     * sets the images of the starting hand leaders.
     * @param leaders the list of leaders extracted by the server.
     */
    public void loadLeaderCards(List<String> leaders){
        ImageView leaderImage;

        for (int i = 0; i < 4; i++) {
            leaderImage = (ImageView) leadersHBox.getChildren().get(i);

            leaderImage.setImage(getSceneProxy().getCardImage(leaders.get(i)));
        }
    }

    /**
     * Adds the clicked leader ({@link MouseEvent#getSource()}) to the list of chosen leaders and puts a glow effect on the image.
     * If there are already two leaders in the list removes the oldest choice.
     * @param mouseEvent the click on the card.
     */
    @FXML
    public void cardSelection(MouseEvent mouseEvent) {
        errorLabel.setOpacity(0);
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

    /**
     * Sets a glow effect on the imageView and increases the size of the imageView, than returns the id of the chosen leader.
     * @param imageView the imageView clicked.
     * @return the id of the chosen leader.
     */
    private String getCardIdAndSelect(ImageView imageView){
        imageView.setEffect(new Glow(0.3));
        imageView.setFitWidth(imageView.getFitWidth()+10);
        imageView.setFitHeight(imageView.getFitHeight()+10);

        return getSceneProxy().getCardID(imageView.getImage());
    }

    /**
     * Checks if the number of chosen leaders is 2 and if it is sends a new message ({@link LeadersChoiceMessage}.
     * If the size of the list is not 2 prints an error message.
     */
    @FXML
    public void sendLeaders() {
        if(leaders.size() != 2) {
            leadersChoiceError("Please choose 2 leaders");
            return;
        }
        (new LeadersChoiceMessage(getClient().getNickname(), leaders)).send();
    }

    /**
     * Resets the scene in case of a wrong choice of the leaders.
     */
    private void resetScene(){
        ImageView view;
        leaders.clear();
        for(Node n : leadersHBox.getChildren()){
            if(n.getEffect() != null){
                view = (ImageView) n;
                view.setEffect(null);
                view.setFitHeight(view.getFitHeight()-10);
                view.setFitWidth(view.getFitWidth()-10);
            }
        }
    }

    /**
     * Sets the text on startingResourcesLabel based on the number of resource that the player has to choose.
     * @param numResources the number of resource that the player has to choose.
     */
    public void loadStartingResources(int numResources){
        startingResources = new ArrayList<>();
        this.numResources = numResources;
        if (numResources == 2)
            startingResourcesLabel.setText("Choose " + numResources + " resources");
        else
            startingResourcesLabel.setText("Choose your resource");
    }

    /**
     * Adds a coin to the list of chosen resources and increases the count on the relative text.
     */
    public void addCoin() {
        ResType coin = ResType.COIN;
        isFinished();

        startingResources.add(coin);
        coinCount.setText("x" + (Integer.parseInt(coinCount.getText().substring(1))+1));
    }

    /**
     * Adds a servant to the list of chosen resources and increases the count on the relative text.
     */
    public void addServant(){
        ResType servant = ResType.SERVANT;
        isFinished();

        startingResources.add(servant);
        servantCount.setText("x" + (Integer.parseInt(servantCount.getText().substring(1))+1));
    }

    /**
     * Adds a shield to the list of chosen resources and increases the count on the relative text.
     */
    public void addShield() {
        ResType shield = ResType.SHIELD;
        isFinished();

        startingResources.add(shield);
        shieldCount.setText("x" + (Integer.parseInt(shieldCount.getText().substring(1))+1));
    }

    /**
     * Adds a coin to the list of chosen resources and increases the count on the relative text.
     */
    public void addStone() {
        ResType stone = ResType.STONE;
        isFinished();

        startingResources.add(stone);
        stoneCount.setText("x" + (Integer.parseInt(stoneCount.getText().substring(1))+1));
    }

    /**
     * Checks if the player has already chosen all the starting resources and in case removes the oldest choice from the list
     * and decreases the count on the relative label.
     */
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

    /**
     * Checks if the size of the list of chosen resources is equals to the number of resources to be chosen and if it's not launch a pop up
     * with a warning message.
     * If the numbers are equals than populates the market buffer and loads the gameScene.
     */
    @FXML
    public void chooseResources() {
        List<PhysicalResource> chosenResources = startingResources.stream()
                .map((x)->new PhysicalResource(x, 1))
                .collect(Collectors.toList());
        if(numResources==chosenResources.size()) {
            getClientController().getMatch().setMarketBuffer(getClient().getNickname(), chosenResources);
            getSceneProxy().changeScene(SceneName.GameScene);
            getSceneProxy().loadStartingMatch();
        }
        else
            JavaFXGUI.popUpWarning("You haven't choose the resource/s yet");
    }

    /**
     * Launch a pop up with a warning message and reset the scene.
     * @param errorMessage the error message to be printed.
     */
    public void leadersChoiceError(String errorMessage){
        JavaFXGUI.popUpWarning(errorMessage);
        resetScene();
    }

}
