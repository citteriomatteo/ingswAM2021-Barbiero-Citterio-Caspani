package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.essentials.Card;
import javafx.application.Platform;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.Client.getClient;


public class SceneProxy {
    private static final SceneProxy instance = new SceneProxy();
    private InitSceneController initSceneController;
    private StartingPhaseSceneController startingPhaseSceneController;
    private TurnSceneController turnSceneController;
    private RematchPhaseSceneController rematchPhaseSceneController;
    private Map<String, Image> idToImageMap;
    private Map<Image, String> imageToIdMap;

    public static SceneProxy getSceneProxy(){ return instance; }

    //%%%%%%%%%%%%%%%%%%%%%%%%SETTER%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public void setInitSceneController(InitSceneController initSceneController) {
        this.initSceneController = initSceneController;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;
    }

    public void setStartingPhaseSceneController(StartingPhaseSceneController startingPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = startingPhaseSceneController;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;

    }

    public void setTurnSceneController(TurnSceneController turnSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = turnSceneController;
        this.rematchPhaseSceneController = null;

    }

    public void setRematchPhaseSceneController(RematchPhaseSceneController rematchPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = rematchPhaseSceneController;

    }

    public void setMap(Map<String, Card> cardMap){
        InputStream imageStream;
        Image image;

        idToImageMap = new HashMap<>();
        imageToIdMap = new HashMap<>();

        for(String cardId :cardMap.keySet()){
            if((cardId.charAt(0) == 'D' && Integer.parseInt(cardId.substring(1)) > 48) || (cardId.charAt(0) == 'L' && Integer.parseInt(cardId.substring(1)) > 16))
                break;
            imageStream = getClass().getResourceAsStream("images/" +
                        ((cardId.startsWith("L")) ? "leaderCards/" : "developmentCards/front/") + cardId +".png");
        if (imageStream != null) {
            image = new Image(imageStream);
            idToImageMap.put(cardId, image);
            imageToIdMap.put(image, cardId);
        }

        }



        //TODO: modify in case of editor
    }

    public Map<String, Image> getIdToImageMap(){
        return idToImageMap;
    }

    public Map<Image, String> getImageToIdMap(){
        return imageToIdMap;
    }

    public void changeScene(SceneName scene){
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(scene.name());
            } catch (IOException e) {
                e.printStackTrace();
                getClient().exit();
            }
        });
    }

    public void loadLeaderCards(List<String> leaders){
        Platform.runLater(()->{
            if(startingPhaseSceneController != null)
                startingPhaseSceneController.loadLeaderCards(leaders);
        });

    }

    public void loginError(String errMessage) {
        Platform.runLater(()->initSceneController.loginError(errMessage));
    }


}
