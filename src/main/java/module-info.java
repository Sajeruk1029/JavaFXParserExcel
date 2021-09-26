module com.example.parserexcelbellsoftmavenfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.apache.poi.poi;

    requires org.apache.commons.compress;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.apache.poi.ooxml;


    opens com.example.parserexcelbellsoftmavenfx to javafx.fxml;
    exports com.example.parserexcelbellsoftmavenfx;
}