package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.GUI.TurnSceneController.populatePlayerPane;

/**
 * Class that handles the display of windows for the scenes and changes the current scene,
 * it contains different static methods for handling the process
 */
public class JavaFXGUI extends Application {
    private static Scene scene;
    private static Stage stage;
    private static Stage popUpStage;
    private static Scene popUpScene;
    private static Stage popUpZoom;
    private static Scene zoom;

    /**
     * Sets up the different possible stages and shows the first scene im the main stage passed -> LoginScene
     * @param stage the primary stage created in {@link Application} methods
     */
    @Override
    public void start(Stage stage) {
        // Show the scene containing the root layout.
        JavaFXGUI.stage = stage;
        try {
            scene = new Scene(loadFXML(SceneName.LoginScene.name()));
        } catch (IOException ignored) {}
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

    /**
     * Changes the root of the current scene, showing a different scene and resizes the window to the relative dimension
     * @param fxml the name of next scene you want to display on the stage, this should be provided by {@link SceneName#name()}
     * @throws IOException if a scene with that name is not found in fxml folder
     */
    static void setRoot(String fxml) throws IOException {
        if(scene == null)
            return ;
        Parent next = loadFXML(fxml);
        scene.setRoot(next);
        stage.sizeToScene();
    }

    /**
     * Creates an additional stage for warning messages that will show the specific error message on necessity without showing it
     */
    private void setUpWarningStage() {
        JavaFXGUI.popUpStage = new Stage();
        try {
            popUpScene = new Scene(loadFXML(SceneName.WarningScene.name()));
        } catch (IOException ignored) {}

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
        populatePlayerPane(player, root);

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

    /**
     * Returns the Parent of the scene if a scene with that name is found in fxml folder
     * @param fxml the name of the scene you want to load
     * @return the Parent of the scene if a scene with that name is found in fxml folder
     * @throws IOException if a scene with that name is not found in fxml folder
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXGUI.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * When the main stage is closed kindly closes the connection with the server sending him a notification message
     */
    @Override
    public void stop() {
        Platform.exit();
        getClient().exit();
        System.exit(0);
    }

}
