package main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    Game gameWorld = new MesoCiv(60, "MesoCiv");

    @Override
    public void start(Stage primaryStage) throws Exception{

        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
