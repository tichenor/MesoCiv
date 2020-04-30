package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public abstract class Game {

    private Scene gameScene;
    private Group sceneNodes;

    // Game loop uses JavaFX's Timeline API
    private static Timeline gameLoop;

    private final int framesPerSecond;
    private final String windowTitle;

    public Game(final int fps, final String title) {
        framesPerSecond = fps;
        windowTitle = title;
        buildAndSetGameLoop();
    }

    protected final void buildAndSetGameLoop() {

        final Duration oneFrameDur = Duration.millis(1000 / (float) getFramesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameDur,
                actionEvent -> {
                    // Loop here
                }); // one frame

        Timeline tl = new Timeline(oneFrame);
        tl.setCycleCount(Animation.INDEFINITE);
        setGameLoop(tl);
    }

    public abstract void initialize(final Stage primaryStage) throws IOException;

    public void beginGameLoop() {
        getGameLoop().play();
    }

    public static Timeline getGameLoop() {
        return gameLoop;
    }

    public static void setGameLoop(Timeline gameLoop) {
        Game.gameLoop = gameLoop;
    }

    public int getFramesPerSecond() {
        return framesPerSecond;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }
}
