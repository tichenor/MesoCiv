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

    public void initializeManagers(Stage stage) throws IOException {
        gameManager.initialize();
        viewManager.initialize(stage, gameManager);
    }

    private void buildAndSetGameLoop() {
        double deltaTimeSeconds = 1 / (double) framesPerSecond;
        final Duration oneFrameDuration = Duration.seconds(deltaTimeSeconds);
        final KeyFrame oneFrame = new KeyFrame(oneFrameDuration,
                actionEvent -> { // The following is run once per frame
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
