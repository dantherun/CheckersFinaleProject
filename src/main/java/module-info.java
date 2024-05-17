module org.example.checkersfinalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
   // requires jfxrt;

    opens org.example.checkersfinalproject to javafx.fxml;
    exports Classes;
    opens Classes to javafx.fxml;
    exports Enums;
    opens Enums to javafx.fxml;
    exports Interface;
    opens Interface to javafx.fxml;
}