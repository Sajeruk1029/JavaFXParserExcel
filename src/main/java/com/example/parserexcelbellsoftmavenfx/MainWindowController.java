package com.example.parserexcelbellsoftmavenfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    public VBox vBox;

    @FXML
    protected void onClickImportBut(ActionEvent event) throws IOException {
        loadFormImport  ();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickExportBut(ActionEvent event) throws IOException {
        loadFormExport();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private void loadFormExport() throws IOException {
        ExportExcelFileApplication exportExcelFileApplication = new ExportExcelFileApplication();
        exportExcelFileApplication.showWindow();
    }

    private void loadFormImport() throws IOException {
        ImportExcelFileApplication importExcelFileApplication = new ImportExcelFileApplication();
        importExcelFileApplication.showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String style = getClass().getResource("/com/example/parserexcelbellsoftmavenfx/style.css").toExternalForm();
        vBox.getStylesheets().add(style);
    }
}