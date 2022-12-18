package project.steganography;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a button to open the first app
        Button app1Button = new Button("Open App 1");
        app1Button.setOnAction(e -> openApp1());

        // Create a button to open the second app
        Button app2Button = new Button("Open App 2");
        app2Button.setOnAction(e -> {
            try {
                new CipherApplication().start(primaryStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add the buttons to a layout
        StackPane root = new StackPane();
        root.getChildren().addAll(app1Button, app2Button);

        // Set up the scene and stage
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Main App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openApp1() {
        // Code to open the first app goes here
    }

    private void openApp2() {
        // Code to open the second app goes here
    }

    public static void main(String[] args) {
        launch(args);
    }
}
