package com.example.parserexcelbellsoftmavenfx;

import com.example.parserexcelbellsoftmavenfx.Configuration.Configuration;
import com.example.parserexcelbellsoftmavenfx.DB.Database;
import com.example.parserexcelbellsoftmavenfx.Parser.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExportExcelFileController implements Initializable {

    @FXML
    public ComboBox<String> templateChooser;

    @FXML
    public VBox vBox;

    @FXML
    protected void onClickExportExcelFileBut(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = null;
        Database database = new Database("sajeruk", "whosyourdaddy", "localhost", 3306, "SimpleDatabase");
        Configuration conf = new Configuration("prop.ini");

        fileChooser.setTitle("Save file...");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));

        file = fileChooser.showSaveDialog((Stage)((Node)event.getSource()).getScene().getWindow());

        if(file == null){
            return;
        }

        database.openConnection();
        conf.openFile();

        if(templateChooser.getValue().equals(conf.getTemplateOne())){
            Parser.createExcelFile(file.getAbsolutePath() + ".xlsx", "0", templateChooser.getValue(), database.getDataTableOne(templateChooser.getValue()));
        }
        else if(templateChooser.getValue().equals(conf.getTemplateTwo())) {
            Parser.createExcelFile(file.getAbsolutePath() + ".xlsx", "0", templateChooser.getValue(), database.getDataTableTwo(templateChooser.getValue()));
        }
        else {
            Parser.createExcelFile(file.getAbsolutePath() +".xlsx", "0", templateChooser.getValue(), database.getDataTableThree(templateChooser.getValue()));
        }

        database.closeConnection();
        conf.closeFile();
    }

    @FXML
    protected void onClickBackBut(ActionEvent event) throws IOException {
        Node node = (Node)event.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        loadFormMainWindow();
        stage.close();
    }

    private void loadFormMainWindow() throws IOException {
        ParserExcelApplication parserExcelApplication = new ParserExcelApplication();
        parserExcelApplication.showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String style = getClass().getResource("/com/example/parserexcelbellsoftmavenfx/style.css").toExternalForm();
        vBox.getStylesheets().add(style);

        Configuration conf = new Configuration("prop.ini");

        conf.openFile();

        ObservableList<String> list = FXCollections.observableArrayList();

        list.add(conf.getTemplateOne());
        list.add(conf.getTemplateTwo());
        list.add(conf.getTemplateThree());

        conf.closeFile();

        templateChooser.setItems(list);
        templateChooser.setValue(list.get(0));
    }
}
