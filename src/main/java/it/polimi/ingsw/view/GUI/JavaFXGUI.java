package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import static it.polimi.ingsw.network.client.Client.getClient;

public class JavaFXGUI extends Application {
    private static Scene scene;
    private static Stage stage;
    private static Stage popUpStage;
    private static Scene popUpScene;
    private static Stage popUpZoom;
    private static Scene zoom;

    @Override
    public void start(Stage stage) throws IOException {
        // Show the scene containing the root layout.
        JavaFXGUI.stage = stage;
        scene = new Scene(loadFXML(SceneName.LoginScene.name()));
        stage.setScene(scene);
        stage.setResizable(false);
        InputStream imageStream = getClass().getResourceAsStream("images/punchBoard/inkwell.png");
        if(imageStream != null)
            stage.getIcons().add(new Image(imageStream));
        stage.setTitle("Masters of Renaissance Board Game");

        setUpWarningStage();
        setUpZoomStage();

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent next = loadFXML(fxml);
        scene.setRoot(next);
        stage.sizeToScene();
    }

    /**
     * Creates an additional stage for warning messages that will show the specific error message on necessity without showing it
     */
    private void setUpWarningStage() throws IOException {
        JavaFXGUI.popUpStage = new Stage();
        popUpScene = new Scene(loadFXML(SceneName.WarningScene.name()));

        popUpStage.setScene(popUpScene);
        popUpStage.setResizable(false);
        InputStream imageStream = JavaFXGUI.class.getResourceAsStream("images/warning.png");
        if(imageStream != null)
            popUpStage.getIcons().add(new Image(imageStream));
        popUpStage.setTitle("Warning");
        popUpStage.setAlwaysOnTop(true);
    }

    /**
     * Creates an additional stage for showing zoomed images of the players' Boards
     */
    private void setUpZoomStage(){
        JavaFXGUI.popUpZoom = new Stage();

        popUpZoom.setResizable(false);
        InputStream imageStream = JavaFXGUI.class.getResourceAsStream("images/zoom.png");
        if(imageStream != null)
            popUpZoom.getIcons().add(new Image(imageStream));
        popUpZoom.setAlwaysOnTop(true);
    }

    /**
     * Shows a pop-up stage for showing a zoom on a specific player board
     * @param player the player you want to show the relative player board
     */
    static void popUpZoom(LightPlayer player){
        popUpZoom.setTitle(player.getNickname()+"'s PlayerBoard");
        try {
            zoom = new Scene(loadFXML(SceneName.ZoomScene.name()));
        } catch (IOException ignored) {}
        popUpZoom.setScene(zoom);
        Pane root = (Pane)zoom.getRoot();
        for(Node n : root.getChildren())
            if(n.getId()!=null)
                switch (n.getId()){
                    case "activeLeaders":
                        TurnSceneController.setActiveLeadersImages(player.getActiveLeaders(), (HBox) n);
                        break;
                    case "marketBuffer":
                        TurnSceneController.populateMarketBuffer(player.getMarketBuffer(), (Pane) n);
                        break;
                    case "handLeaders":
                        TurnSceneController.clearHandLeadersImages(player.getHandLeaders(), (HBox) n);
                        break;
                    case "faithPath":
                        GridPane faithPath = (GridPane) n;
                        faithPath.getChildren().get(player.getFaithMarker()).setVisible(true);
                        break;
                    case "devCardSlots":
                        TurnSceneController.populateDevCardSlots(player.getDevCardSlots(), (HBox) n);
                        break;
                    case "warehousePane":
                        TurnSceneController.populateWarehouse(player.getWarehouse(), (Pane) n);
                        break;
                    case "strongBox":
                        TurnSceneController.populateStrongbox(player.getStrongbox(), (VBox) n);

                }

        popUpZoom.show();
    }

    /**
     * Shows a pop-up stage for warning messages showing the specific error passed
     * @param error the string you want to show
     */
    static void popUpWarning(String error){
        Pane root = (Pane)popUpScene.getRoot();
        Label label = (Label) SceneProxy.getChildById(root, "errorLabel");
        label.setText(error);

        popUpStage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXGUI.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() {
        Platform.exit();
        getClient().exit();
        System.exit(0);
    }

}
