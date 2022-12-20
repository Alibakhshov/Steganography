package project.steganography.Contact;

import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class WhatsApp {


    public void start(Stage stage) {
        String phoneNumber = "9312345678";
        try {
            URL url = new URL("https://wa.me/" + phoneNumber);
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}