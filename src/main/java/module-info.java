module project.steganography {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires javafx.swing;

    opens project.steganography to javafx.fxml;
    exports project.steganography;
    exports project.steganography.PictureSteganography;
    opens project.steganography.PictureSteganography to javafx.fxml;
//    exports project.steganography.CipherApplication;
//    opens project.steganography.CipherApplication to javafx.fxml;
//    exports project.steganography.Cipher;
//    opens project.steganography.Cipher to javafx.fxml;
}