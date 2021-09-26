package com.example.parserexcelbellsoftmavenfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExportExcelFileApplication extends Application {

    Stage stage = new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ExportExcelFile.fxml"));
        stage.setTitle("Export");
        stage.setScene(new Scene(root, 320, 240));
        stage.show();
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }

    public void showWindow() throws IOException {
        start(stage);
    }
}
