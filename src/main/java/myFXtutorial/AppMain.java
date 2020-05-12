package main.java.myFXtutorial;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MesoCiv game = new MesoCiv(60); // 60 fps
        game.initializeManagers(primaryStage); // Initialize game model and game window
        primaryStage.show(); // Make the window visible
        game.beginGameLoop();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
