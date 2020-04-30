package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MesoCiv extends Game {

    public MesoCiv(int fps, String title) {
        super(fps, title);
    }

    @Override
    public void initialize(Stage primaryStage) throws IOException {

        String resourcePath = "sample.fxml";
        URL location = getClass().getResource(resourcePath);
        Parent root = FXMLLoader.load(location);

        primaryStage.setScene(new Scene(root, 1920, 1080));

    }
}
