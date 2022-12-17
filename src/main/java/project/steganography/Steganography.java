package project.steganography;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Steganography extends Application {

    // GUI components
    private Label messageLabel;
    private TextField messageField;
    private Button selectImageButton;
    private Button encodeButton;
    private Button decodeButton;
    private File imageFile;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // create GUI components
        messageLabel = new Label("Enter message: ");
        messageField = new TextField();
        selectImageButton = new Button("Select image");
        encodeButton = new Button("Encode");
        decodeButton = new Button("Decode");

        // set action for Select Image button
        selectImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select image file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            imageFile = fileChooser.showOpenDialog(primaryStage);
        });

        // set action for Encode button
        encodeButton.setOnAction(event -> {
            if (imageFile == null) {
                System.out.println("Please select an image file first.");
                return;
            }
            String message = messageField.getText();
            if (message.isEmpty()) {
                System.out.println("Please enter a message to encode.");
                return;
            }
            try {
                BufferedImage image = ImageIO.read(imageFile);
                // perform steganography encoding here
                // ...

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save encoded image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );
                File outputFile = fileChooser.showSaveDialog(primaryStage);
                if (outputFile != null) {
                    ImageIO.write(image, "png", outputFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // set action for Decode button
        decodeButton.setOnAction(event -> {
            if (imageFile == null) {
                System.out.println("Please select an image file first.");
                return;
            }
            try {
                BufferedImage image = ImageIO.read(imageFile);
                // perform steganography decoding here

                String message = "decoded message";
                System.out.println("Decoded message: " + message);
            } catch (IOException e) {
                e.printStackTrace();


            }
        });

        // create a layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageLabel, messageField, selectImageButton, encodeButton, decodeButton);

        // create a scene
        Scene scene = new Scene(layout, 300, 250);

        // set the scene
        primaryStage.setScene(scene);
        primaryStage.setTitle("Steganography");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
