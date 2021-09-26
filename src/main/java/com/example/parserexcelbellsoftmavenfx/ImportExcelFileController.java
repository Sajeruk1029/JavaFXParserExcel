package com.example.parserexcelbellsoftmavenfx;

import com.example.parserexcelbellsoftmavenfx.Configuration.Configuration;
import com.example.parserexcelbellsoftmavenfx.DB.Database;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateOne;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateThree;
import com.example.parserexcelbellsoftmavenfx.Models.TemplateTwo;
import com.example.parserexcelbellsoftmavenfx.Parser.Parser;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImportExcelFileController implements Initializable {

    @FXML
    public TableView<ObservableList<String>> table;
    @FXML
    public ProgressBar progress;
    @FXML
    public ComboBox<String> templateChooser;
    @FXML
    public Label nameTable;
    @FXML
    public VBox vBox;

    private File file;

    @FXML
    protected void onClickExportBut(ActionEvent event) throws InterruptedException, IOException {

        if(file == null){
            return;
        }

        Parser parser = new Parser(file.getAbsolutePath());
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        Configuration conf = new Configuration("prop.ini");
        Database db = new Database("sajeruk", "whosyourdaddy", "localhost", 3306, "SimpleDatabase");
        ArrayList<ArrayList<String>> data = null;
        ArrayList<TemplateOne> listTemplateOne = new ArrayList<TemplateOne>();
        ArrayList<TemplateTwo> listTemplateTwo = new ArrayList<TemplateTwo>();
        ArrayList<TemplateThree> listTemplateThree = new ArrayList<TemplateThree>();
        long sleep = 0;

        progress.setOpacity(1);

        parser.openFile();
        conf.openFile();
        db.openConnection();

        parser.focusingOnSheet();

        System.out.println(conf.getSleep());
        sleep = Long.parseLong(conf.getSleep());

        list = parser.getLinesStringValues(4,9, 1,0);
        data = parser.getSelectedDataString(parser.getNumbersCells(templateChooser.getValue(), parser.getLineStringValues(9, 0)), parser.getLinesStringValues(4,9, 1, 0));

        if(templateChooser.getValue().equals(conf.getTemplateOne()))
        {
            for(int counter = 0; counter < data.size(); ++counter){
                listTemplateOne.add(new TemplateOne(0, data.get(counter).get(0), data.get(counter).get(1)));
            }
        }
        else if(templateChooser.getValue().equals(conf.getTemplateTwo())){
            for(int counter = 0; counter < data.size(); ++counter){
                listTemplateTwo.add(new TemplateTwo(0, data.get(counter).get(0), data.get(counter).get(1), (long)Double.parseDouble(data.get(counter).get(2))));
            }
        }
        else {
            for (int counter = 0; counter < data.size(); ++counter) {
                listTemplateThree.add(new TemplateThree(0, data.get(counter).get(0), data.get(counter).get(1), (long)Double.parseDouble(data.get(counter).get(2)), Boolean.getBoolean(data.get(counter).get(3))));
            }
        }

        long  finalSleep = sleep;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                System.out.println(finalSleep);

                if(templateChooser.getValue().equals(conf.getTemplateOne()))
                {
                    for (int counter = 0; counter < listTemplateOne.size(); ++counter){
                        db.insertDataTableOne(listTemplateOne.get(counter).getName(), listTemplateOne.get(counter).getDescription());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalProg = (1.0 / listTemplateOne.size());;
                        Platform.runLater(() -> progress.setProgress(finalProg + progress.getProgress()));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if(templateChooser.getValue().equals(conf.getTemplateTwo())){
                    for (int counter = 0; counter < listTemplateTwo.size(); ++counter){
                        db.insertDataTableTwo(listTemplateTwo.get(counter).getName(), listTemplateTwo.get(counter).getDescription(), listTemplateTwo.get(counter).getValue());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalProg1 = (1.0 / listTemplateTwo.size());
                        System.out.println(finalProg1);
                        Platform.runLater(() -> progress.setProgress(progress.getProgress() + finalProg1));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            System.out.println("f");
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    for (int counter = 0; counter < listTemplateThree.size(); ++counter){
                        db.insertDataTableThree(listTemplateThree.get(counter).getName(), listTemplateThree.get(counter).getDescription(), listTemplateThree.get(counter).getValue(), listTemplateThree.get(counter).getAvailability());
                        System.out.println(progress.getProgress());
                        System.out.println();

                        double finalProg2 = (1.0 / listTemplateThree.size());
                        Platform.runLater(() -> progress.setProgress(progress.getProgress() + finalProg2));

                        try {
                            Thread.sleep(finalSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                this.succeeded();
                return null;
            }
        };

        task.setOnSucceeded((WorkerStateEvent eventTask) -> {
            progress.setProgress(0.0);
            progress.setOpacity(0);

            try {
                parser.closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conf.closeFile();

            db.closeConnection();
        });

        Thread thread = new Thread(task);

        thread.start();
    }

    @FXML
    protected void onClickImportBut(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parser parser;
        Database db;
        ObservableList<ObservableList<String>> list;
        ArrayList<ArrayList<String>> listData;

        table.getColumns().clear();

        fileChooser.setTitle("Set Excel File...");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx"));

        file = fileChooser.showOpenDialog(stage);

        if(file == null){
            return;
        }

        parser = new Parser(file.getAbsolutePath());

        parser.openFile();

        parser.focusingOnSheet();

        list = FXCollections.observableArrayList();

        listData = parser.getLinesStringValues(4,9);

        for(int counter = 1; counter < listData.size(); ++counter){
            list.add(FXCollections.observableArrayList(listData.get(counter)));
        }

        table.setItems(list);

        for (int counter = 0; counter < listData.get(0).size(); ++counter){
            final int index = counter;

            TableColumn<ObservableList<String>, String> column = new TableColumn<ObservableList<String>, String>(listData.get(0).get(index));
            column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));
            column.setEditable(false);
            column.setOnEditCommit((TableColumn.CellEditEvent<ObservableList<String>, String> t) -> {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).set(t.getTablePosition().getColumn(), t.getNewValue());
            } );
            table.getColumns().add(column);
        }

        parser.closeFile();
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

        table.getStylesheets().add(style);

        templateChooser.getScene();

        Configuration conf = new Configuration("prop.ini");

        conf.openFile();

        ObservableList<String> list = FXCollections.observableArrayList();

        list.add(conf.getTemplateOne());
        list.add(conf.getTemplateTwo());
        list.add(conf.getTemplateThree());

        nameTable.setText(conf.getTable());

        conf.closeFile();

        templateChooser.setItems(list);
        templateChooser.setValue(list.get(0));
    }
}