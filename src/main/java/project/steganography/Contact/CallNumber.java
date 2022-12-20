package project.steganography.Contact;

import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class CallNumber {
    public static void main(String[] args) {
        String phoneNumber = "123-456-7890";
        try {
            Desktop.getDesktop().browse(new URI("tel:" + phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Stage primaryStage) {
        String phoneNumber = "123-456-7890";
        try {
            Desktop.getDesktop().browse(new URI("tel:" + phoneNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}