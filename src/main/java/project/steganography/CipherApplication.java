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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        Button clearButton = new Button("Clear");
        Button exitButton = new Button("Exit");
        Button aboutButton = new Button("About");
        Button helpButton = new Button("Help");
        Button saveButton = new Button("Save");
        Button backToMain = new Button("Back to Main");

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

        // Set the action for the clear button
        clearButton.setOnAction(event -> {
            keyField.clear();
            inputArea.clear();
            outputArea.clear();
        });

        // Set the action for the exit button
        exitButton.setOnAction(event -> {
            System.exit(0);
        });

        // Set the action for the about button
        aboutButton.setOnAction(event -> {
            outputArea.setText("This is a simple cipher application that uses a Caesar cipher to \nencode and decode messages. " +
                    "The key is a number that is used to shift the letters in \nthe message. For example, if the key is 3, then " +
                    "the letter A would be replaced by \nD, B would be replaced by E, and so on. The key can be any number from " +
                    "1 to 25. \nThe application will encode and decode messages using the key that you enter. \nYou can " +
                    "also use the application to encode and decode messages that you \nfind on the Internet. The application " +
                    "will not work if you enter a key \nthat is not a number between 1 and 25.");
        });

        // Set the action for the help button
        helpButton.setOnAction(event -> {
            outputArea.setText("Enter a message in the input area and a key in the key field. " +
                    "\nThen click the Encode button to encode the message. To decode a message, " +
                    "\nenter the encoded message in the input area and the key that was used to " +
                    "\nencode the message in the key field. Then click the Decode button.");
        });

        // Set the action for the save button
        saveButton.setOnAction(event -> {
            String outputAreaText = outputArea.getText();
            String inputAreaText = inputArea.getText();
            File outputFile = new File("output.txt");
            File inputFile = new File("input.txt");
            try {
                Files.write(Paths.get(outputFile.getAbsolutePath()), outputAreaText.getBytes());
                Files.write(Paths.get(inputFile.getAbsolutePath()), inputAreaText.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Set the action for the back to main button
        backToMain.setOnAction(e -> {
            try {
                new SteganographyApp().start(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Create the button box
        HBox buttonBox = new HBox(10, encodeButton, decodeButton, clearButton, exitButton, aboutButton, helpButton, saveButton, backToMain);
        buttonBox.setAlignment(Pos.CENTER);

        // Create the root layout
        VBox root = new VBox(10, new Label("Cipher Key:"), keyField, inputArea, outputArea, buttonBox);
        root.setPadding(new Insets(10));

        // Set the scene and show the stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Cipher Application");
        primaryStage.show();
    }

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
