package project.steganography.PictureSteganography;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MenuLayout {

  public static Pane layout(Stage window) {
    HBox layout = new HBox(5);
    layout.setStyle(
            "-fx-background-color: #f0f3f0;"
            + "-fx-border-color: #f1dddd;"
            + "-fx-border-width: 5;"
            + "-fx-border-style: solid;"
    );
    layout.setPrefSize(Steganography.WIDTH * 0.25, Steganography.HEIGHT * 0.1);

    Font font = new Font("Arial", Steganography.HEIGHT / 50);

    Button hideButton = new Button("Hide File");
    hideButton.setStyle(
            "-fx-background-color: #006c00;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
    );
    hideButton.setFont(font);
    hideButton.setMinWidth(layout.getPrefWidth() / 2);
    hideButton.setMaxHeight(Double.MAX_VALUE);
    hideButton.setOnAction(e -> window.setScene(new Scene(HiderLayout.layout(window))));

    Button revealButton = new Button("Reveal File");
    revealButton.setStyle(
            "-fx-background-color: #006c00;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;"
    );
    revealButton.setFont(font);
    revealButton.setMinWidth(layout.getPrefWidth() / 2);
    revealButton.setMaxHeight(Double.MAX_VALUE);
    revealButton.setOnAction(e -> window.setScene(new Scene(RevealerLayout.layout(window))));

    layout.getChildren().addAll(hideButton, revealButton);

    return layout;
  }

}
