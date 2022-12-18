package project.steganography;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Steganography1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TextField textField = new TextField();
        Button saveButton = new Button("Save");

        // Add an action to the save button that saves the text in the textfield to a file
        saveButton.setOnAction(event -> {
            String text = textField.getText();
            File file = new File("output.txt");
            try {
                Files.write(Paths.get(file.getAbsolutePath()), text.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        VBox root = new VBox(textField, saveButton);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Save Button Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
