package main.java.myFXtutorial;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the top-most class for everything related to the game. It consists of a GameManager, which handles
 * the logic and mechanics of the game (numbers, calculations, progress, etc; the internal game model) and a
 * ViewManager, which handles loading the FXML, different scenes (not yet implemented) and updating the controller.
 * This class also contains the 'game loop', which is a method that is called the number of times per second specified
 * by the 'fps' (frames per second) in the constructor. The game loop first updates the GameManager, which progresses
 * the internal game model, and then updates the ViewManager who in turn updates the GUI elements in the controller
 * class.
 */
public class MesoCiv {

    private GameManager gameManager;
    private ViewManager viewManager;

    private static Timeline gameLoop;

    private final int framesPerSecond;

    public MesoCiv(final int fps) {
        framesPerSecond = fps;
        gameManager = new GameManager();
        viewManager = new ViewManager();
        buildAndSetGameLoop();
    }

    /**
     * This method runs initializes the GameManager and ViewManager and creates a reference to the GameManager in the
     * FXML controller class. This method should be run right after a new instance of this class is created (in other
     * words, right after the constructor).
     * @param stage The primary window that the ViewManager should load the FXML into (the "game" window).
     * @throws IOException
     */
    public void initializeManagers(Stage stage) throws IOException {
        gameManager.initialize();
        viewManager.initialize(stage);
        viewManager.getController().setGameManager(gameManager);
    }

    private void buildAndSetGameLoop() {
        double deltaTimeSeconds = 1 / (double) framesPerSecond;
        final Duration oneFrameDuration = Duration.seconds(deltaTimeSeconds);
        final KeyFrame oneFrame = new KeyFrame(oneFrameDuration,
                actionEvent -> { // The following is run once per frame; the game loop
                    gameManager.update(deltaTimeSeconds);
                    viewManager.update(deltaTimeSeconds);
                });
        Timeline tl = new Timeline(oneFrame);
        tl.setCycleCount(Animation.INDEFINITE);
        gameLoop = tl;
    }

    public void beginGameLoop() {
        gameLoop.play();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }
}
