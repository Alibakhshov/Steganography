package project.steganography;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import project.steganography.Contact.CallNumber;
import project.steganography.PictureSteganography.Steganography;

public class SteganographyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Setting the title of the stage
        primaryStage.setTitle("Steganography App");

        // Creating a label for the title
        Label titleLabel = new Label("Steganography App");
        titleLabel.setFont(new Font(24));

        Image image = new Image("file:src/main/resources/project/steganography/images/phone-call.png");
        ImageView phoneCallImage = new ImageView(image);
        phoneCallImage.setFitHeight(50);
        phoneCallImage.setFitWidth(50);

        Button phoneCallButton = new Button("", phoneCallImage);
        phoneCallButton.setPrefSize(200, 10);
        phoneCallButton.setStyle(
                "-fx-background-color: none;" +
                        "-fx-cursor: pointer;"

        );
        phoneCallButton.setOnAction(e -> {
            try {
                new CallNumber().start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });




        // Create buttons for the main actions
        Button cipherButton = new Button("Cipher");
        cipherButton.setOnAction(e -> {
            try {
                new CipherApplication().start(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        Button picButton = new Button("Picture Steganography");
        picButton.setOnAction(e -> {
            try {
                new Steganography().start(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException("Error");
            }
        });

        // Add some padding to the buttons
        cipherButton.setPadding(new Insets(10));
        picButton.setPadding(new Insets(10));

        // Create a horizontal box to hold the buttons
        HBox buttonBox = new HBox(20, cipherButton, picButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create a vertical box to hold the title and buttons
        VBox mainBox = new VBox(20, titleLabel, buttonBox, phoneCallImage, phoneCallButton);
        mainBox.setPadding(new Insets(20));
        mainBox.setAlignment(Pos.CENTER);

        // Create a border pane to hold the main box
        BorderPane root = new BorderPane();
        root.setCenter(mainBox);



        // Set the scene and show the stage
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
