package project.steganography.Contact;

import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

public class LinkedIn {
    public void start(Stage stage) {
        String url = "https://www.linkedin.com/in/alibakhshov";
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

