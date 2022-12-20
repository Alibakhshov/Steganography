package project.steganography.Contact;

import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

public class Github {
    public void start(Stage stage) {
        String url = "https://github.com/Alibakhshov";
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
