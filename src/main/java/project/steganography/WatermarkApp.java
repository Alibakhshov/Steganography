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

        uploadButton = new Button("Upload Image");
        uploadButton.setOnAction(event -> uploadImage());

        addWatermarkButton = new Button("Add Watermark");
        addWatermarkButton.setOnAction(event -> addWatermark());

        HBox hbox = new HBox(10, label, textField);
        VBox vbox = new VBox(10, uploadButton, hbox, addWatermarkButton);

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        VBox root = new VBox(20, vbox, imageView);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
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