package com.example.parserexcelbellsoftmavenfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ParserExcelApplication extends Application {

    Stage stage = new Stage();

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ParserExcelApplication.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("MainWindow");
        stage.setScene(scene);
        stage.show();
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }

    public void showWindow() throws IOException {
        start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}