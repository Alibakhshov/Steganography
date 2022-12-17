package project.steganography;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CipherApplication extends Application {

    private TextField keyField;
    private TextArea inputArea;
    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create the key field
        keyField = new TextField();
        keyField.setPromptText("Enter cipher key");
        keyField.setMaxWidth(200);

        // Create the input and output text areas
        inputArea = new TextArea();
        inputArea.setPromptText("Enter message to encode or decode");
        outputArea = new TextArea();
        outputArea.setPromptText("Result will appear here");
        outputArea.setEditable(false);

        // Create the encode and decode buttons
        Button encodeButton = new Button("Encode");
        Button decodeButton = new Button("Decode");

        // Set the action for the encode button
        encodeButton.setOnAction(event -> {
            String key = keyField.getText();
            String input = inputArea.getText();
            String encoded = encode(input, Integer.parseInt(key));
            outputArea.setText(encoded);
        });

        // Set the action for the decode button
        decodeButton.setOnAction(event -> {
            String key = keyField.getText();
            String input = inputArea.getText();
            String decoded = decode(input, Integer.parseInt(key));
            outputArea.setText(decoded);
        });

        // Create the button box
        HBox buttonBox = new HBox(10, encodeButton, decodeButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create the root layout
        VBox root = new VBox(10, new Label("Cipher Key:"), keyField, inputArea, outputArea, buttonBox);
        root.setPadding(new Insets(10));

        // Set the scene and show the stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Cipher Application");
        primaryStage.show();
    }

//    private String encode(String input, String key) {
//        // Implement the substitution cipher encoding here
//        return " ";
//    }
//
//    private String decode(String input, String key) {
//        // Implement the substitution cipher decoding here
//        return "";
//    }
//}
    private String encode(String input, int key) {
        // Shift each character in the message by the shift amount
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : input.toCharArray()) {
            encodedMessage.append((char) (c + key));
        }
        return encodedMessage.toString();
    }

    private  String decode(String input, int key) {
        // Shift each character in the message by the shift amount
        StringBuilder decodedMessage = new StringBuilder();
        for (char c : input.toCharArray()) {
            decodedMessage.append((char) (c - key));
        }
        return decodedMessage.toString();
    }
}
