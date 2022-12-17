package project.steganography;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Steganography1 extends Application {
    private static final String DEFAULT_IMAGE_PATH = "./image.png";
    private static final String STEGANOGRAPHY_IMAGE_FORMAT = "png";
    private static final String STEGANOGRAPHY_IMAGE_PATH = "./steganography.png";

    private BufferedImage image;
    private TextField messageField;
    private Button hideButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // Set up the text field for the message
        messageField = new TextField();
        messageField.setPromptText("Enter message to hide");

        // Set up the hide button
        hideButton = new Button();
        hideButton.setText("Hide Message");
        hideButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Get the message to hide
                    String message = messageField.getText();

                    // Hide the message in the image
                    BufferedImage steganographyImage = hideMessage(image, message);

                    // Save the steganography image
                    ImageIO.write(steganographyImage, STEGANOGRAPHY_IMAGE_FORMAT, new File(STEGANOGRAPHY_IMAGE_PATH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Set up the root pane
        StackPane root = new StackPane();
        root.getChildren().add(messageField);
        root.getChildren().add(hideButton);

        // Set up the scene
        Scene scene = new Scene(root, 300, 250);

        // Set up the stage
        primaryStage.setTitle("Steganography");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

        // Load the image
        image = ImageIO.read(new File(DEFAULT_IMAGE_PATH));
    }

    private BufferedImage hideMessage(BufferedImage image, String message) {

        return image;



    }
}



