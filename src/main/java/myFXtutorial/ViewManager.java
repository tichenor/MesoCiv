package main.java.myFXtutorial;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class ViewManager {

    public static final int SCENE_WIDTH = 1920, SCENE_HEIGHT = 1080;
    private static final String WINDOW_TITLE = "MesoCiv";
    private static final String FXML_PATH = "/mainScene.fxml";
    private static final String CSS_PATH = "/app.css";

    private Stage primaryStage;
    private Scene primaryScene;

    private Controller controller;

    private double internalTimer = 0.0;

    public ViewManager() {
    }

    public void initialize(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle(WINDOW_TITLE);

        // Add icon to window top bar
        InputStream is = getClass().getResourceAsStream("/other/icons8-casa-batllo-48.png");
        primaryStage.getIcons().add(new Image(is));

        // Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(FXML_PATH));
        Parent root = loader.load();

        // Fetch the controller and pass it an instance of the game manager.
        controller = loader.getController();

        // Create the primary scene (content of primary stage) from the fxml file.
        primaryScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        primaryScene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());

        // Add the scene to the stage.
        primaryStage.setScene(primaryScene);

    }

    public void update(double seconds) {
        internalTimer += seconds;
        while (internalTimer > 1) {
            controller.updateAll();
            internalTimer -= 1;
        }
    }

    public Controller getController() {
        return controller;
    }

}
