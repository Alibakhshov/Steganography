module project.steganography {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens project.steganography to javafx.fxml;
    exports project.steganography;
    exports project.steganography.run;
    opens project.steganography.run to javafx.fxml;
}