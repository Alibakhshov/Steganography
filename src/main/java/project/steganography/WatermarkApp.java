package project.steganography;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class WatermarkApp extends Application {

    private Image image;
    private ImageView imageView;
    private TextField textField;
    private Label label;
    private Button uploadButton;
    private Button addWatermarkButton;

    private final double[][] watermark = {
            {0.5, 0, 0},
            {0, 0.5, 0},
            {0, 0, 0.5}
    };

    @Override
    public void start(Stage primaryStage) {

        label = new Label("Enter text for watermark:");
        textField = new TextField();
        textField.setPromptText("Enter text here");
        textField.setPrefWidth(200);
        textField.setPrefHeight(25);
        textField.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #000000;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-padding: 0 0 0 0;"

        );

        uploadButton = new Button("Upload Image");
        uploadButton.setStyle(
                "-fx-font-size: 1.2em; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #00ff00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 1px; "  +
                        "-fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI';"
        );
        uploadButton.setOnAction(event -> uploadImage());

        addWatermarkButton = new Button("Add Watermark");
        addWatermarkButton.setStyle(
                "-fx-font-size: 1.2em; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #00ff00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 1px; "  +
                        "-fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI';"
        );
        addWatermarkButton.setOnAction(event -> addWatermark());

        Button saveButton = new Button("Save Image");
        saveButton.setStyle(
                "-fx-font-size: 1.2em; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #00ff00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 1px; "  +
                        "-fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI';"
        );
        saveButton.setOnAction(event -> saveImage());

        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 1.2em; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #00ff00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 1px; "  +
                        "-fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI';"
        );
        backButton.setOnAction(event -> {
            try {
                new SteganographyApp().start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button deleteImageButton = new Button("Delete Image");
        deleteImageButton.setStyle(
                "-fx-font-size: 1.2em; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #00ff00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-border-color: #000000; " +
                        "-fx-border-width: 1px; "  +
                        "-fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI';"
        );
        deleteImageButton.setOnAction(event -> {
            image = null;
            imageView.setImage(null);
        });

        HBox buttonBox = new HBox(10, uploadButton, addWatermarkButton, saveButton, deleteImageButton, backButton);
        VBox vbox = new VBox(10, label, textField, buttonBox);

        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        VBox root = new VBox(20, vbox, imageView);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 600, 200);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double imageWidth = imageView.getFitWidth();
            double vboxWidth = vbox.getBoundsInParent().getWidth();
            double newImageWidth = Math.min(imageWidth, newVal.doubleValue() - vboxWidth - 40); // 40 is the total horizontal padding
            imageView.setFitWidth(newImageWidth);
        });
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            double imageHeight = imageView.getFitHeight();
            double newImageHeight = Math.min(imageHeight, newVal.doubleValue() - 60); // 60 is the total vertical padding
            imageView.setFitHeight(newImageHeight);
        });
        primaryStage.setScene(scene);
        primaryStage.show();





//        HBox buttonBox = new HBox(10, uploadButton, addWatermarkButton, saveButton, deleteImageButton, backButton);
//        VBox vbox = new VBox(10, label, textField, buttonBox);
//
//        imageView = new ImageView();
//        imageView.setFitWidth(200);
//        imageView.setPreserveRatio(true);
//
//        VBox root = new VBox(20, vbox, imageView);
//        root.setAlignment(Pos.CENTER);
//        Scene scene = new Scene(root, 600, 200);
//        primaryStage.setScene(scene);
//        primaryStage.show();

    }

    private void saveImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addWatermark() {
        if (image == null) {
            return;
        }

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        String text = textField.getText();
        if (text.length() == 0) {
            return;
        }

        try {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    int alpha = (rgb >> 24) & 0xFF;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    int[][] pixel = {{red}, {green}, {blue}};
                    int[][] newPixel = matrixMultiply(watermark, pixel);
                    newBufferedImage.setRGB(x, y, (alpha << 24) | (newPixel[0][0] << 16) | (newPixel[1][0]
                            << 8) | newPixel[2][0]);
                }
            }

            Graphics2D g2d = newBufferedImage.createGraphics();
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, 10, 30);
            g2d.dispose();

            image = SwingFXUtils.toFXImage(newBufferedImage, null);
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int[][] matrixMultiply(double[][] a, int[][] b) throws IllegalArgumentException {
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("Invalid matrix dimensions");
        }

        int m = a.length;
        int n = b[0].length;
        int[][] c = new int[m][n];

        try {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < a[0].length; k++) {
                        sum += a[i][k] * b[k][j];
                    }
                    c[i][j] = (int) Math.round(sum);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return c;
    }

    public static void main(String[] args) {
        launch(args);
    }
}