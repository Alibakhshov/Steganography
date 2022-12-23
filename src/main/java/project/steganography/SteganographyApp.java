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
import project.steganography.Contact.Github;
import project.steganography.Contact.LinkedIn;
import project.steganography.Contact.WhatsApp;
import project.steganography.PictureSteganography.Steganography;

public class SteganographyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Setting the title of the stage
        primaryStage.setTitle("Steganography App");

        // Creating a label for the title
        Label titleLabel = new Label("Steganography App");
        titleLabel.setFont(new Font("Arial", 30));
        titleLabel.setPadding(new Insets(-90, 10, 0, 100));


        // Adding an image to the Phone Call button
        Image phoneImage = new Image("file:src/main/resources/project/steganography/images/phone-call.png");
        ImageView phoneCallImage = new ImageView(phoneImage);
        phoneCallImage.setFitHeight(20);
        phoneCallImage.setFitWidth(20);
//        phoneCallImage.setLayoutX(50);
//        phoneCallImage.setLayoutY(10);



        // Creating a button for the phone call
        Button phoneCallButton = new Button("", phoneCallImage);
        phoneCallButton.setLayoutX(100);
        phoneCallButton.setLayoutY(10);
//        phoneCallButton.setPrefSize(200, 10);
        phoneCallButton.setStyle(
                        "-fx-background-color: none;" +
                        "-fx-cursor: hand;"

        );
        phoneCallButton.setOnAction(e -> {
            try {
                new CallNumber().start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Adding an image to the Whatsapp button
        Image whatsImage = new Image("file:src/main/resources/project/steganography/images/whatsapp.png");
        ImageView whatsAppImage = new ImageView(whatsImage);
        whatsAppImage.setFitHeight(20);
        whatsAppImage.setFitWidth(20);


        // Creating a button for the whatsapp
        Button whatsAppButton = new Button("", whatsAppImage);
        whatsAppButton.setPrefSize(200, 10);
        whatsAppButton.setStyle(
                        "-fx-background-color: none;" +
                        "-fx-cursor: hand;"

        );
        whatsAppButton.setOnAction(e -> {
            try {
                new WhatsApp().start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Creating a button for exit
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(200, 10);
        exitButton.setOnAction(e -> {
            System.exit(0);
        });


        // Adding an image to the github button
        Image gitImage = new Image("file:src/main/resources/project/steganography/images/github.png");
        ImageView githubImage = new ImageView(gitImage);
        githubImage.setFitHeight(20);
        githubImage.setFitWidth(20);


        // Creating a button for the github
        Button githubButton = new Button("", githubImage);
        githubButton.setPrefSize(200, 10);
        githubButton.setStyle(
                        "-fx-background-color: none;" +
                        "-fx-cursor: hand;"

        );
        githubButton.setOnAction(e -> {
            try {
                new Github().start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Adding an image to the linkedin button
        Image linkImage = new Image("file:src/main/resources/project/steganography/images/linkedin.png");
        ImageView linkedinImage = new ImageView(linkImage);
        linkedinImage.setFitHeight(20);
        linkedinImage.setFitWidth(20);


        // Creating a button for the linkedin
        Button linkedinButton = new Button("", linkedinImage);
        linkedinButton.setPrefSize(200, 10);
        linkedinButton.setStyle(
                        "-fx-background-color: none;" +
                        "-fx-cursor: hand;"

        );
        linkedinButton.setOnAction(e -> {
            try {
                new LinkedIn().start(new Stage());
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

        // Creating button for the Picture Steganography App
        Button picButton = new Button("Picture Steganography");
        picButton.getStyleClass().add("picButton");
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
        HBox buttonBox = new HBox(20, cipherButton, picButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Creating a right vertical box to hold the picture
        VBox rightBox = new VBox(20, githubImage);
        rightBox.setStyle(
                "-fx-background-color: #000000;"
//                "-fx-background-radius: 2 5 5 0;" +
//                "-fx-padding: 50;" +
//                "-fx-spacing: 20;"
        );
        rightBox.setPadding(new Insets(200, 10, 10, 10));
//        rightBox.setAlignment(Pos.CENTER_RIGHT);




        // Create a horizontal box to hold the contact buttons
        HBox contactBox = new HBox(20,
                phoneCallImage,
                phoneCallButton,
                whatsAppImage,
                whatsAppButton,
                githubImage,
                githubButton,
                linkedinImage,
                linkedinButton);
        contactBox.setPadding(new Insets(10));
        contactBox.setAlignment(Pos.CENTER);

        // Create a vertical box to hold project picture
        Image projectImage = new Image("file:src/main/resources/project/steganography/images/github.png");
        ImageView projectImageView = new ImageView(projectImage);
        projectImageView.setLayoutX(100);
        projectImageView.setLayoutY(10);
        projectImageView.setFitHeight(50);
        projectImageView.setFitWidth(50);
        VBox projectBox = new VBox(20,   projectImageView, titleLabel);
        projectBox.setPadding(new Insets(10, 10, 10, 10));
        projectBox.setAlignment(Pos.TOP_LEFT);


        // Create a vertical box to hold the title and buttons
        VBox mainBox = new VBox(20, buttonBox);
        mainBox.setPadding(new Insets(20));
        mainBox.getChildren().add(linkedinImage);
        mainBox.setAlignment(Pos.CENTER);

        // Create a border pane to hold the main box
        BorderPane root = new BorderPane();
        root.setCenter(mainBox);
        root.setBottom(contactBox);
        root.setTop(projectBox);
        root.setLeft(rightBox);

        // Set the scene and show the stage
        Scene scene = new Scene(root, 900, 300);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
