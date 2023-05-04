package project.steganography;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction extends Application {

    private final int HASH_LENGTH = 16;
    private final int MATRIX_SIZE = 4;

    @Override
    public void start(Stage primaryStage) {

        // Create UI elements
        Label messageLabel = new Label("Message:");
        TextField messageField = new TextField();
        Button hashButton = new Button("Hash");
        Label hashLabel = new Label();

        // Create GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Add UI elements to GridPane
        gridPane.add(messageLabel, 0, 0);
        gridPane.add(messageField, 1, 0);
        gridPane.add(hashButton, 0, 1);
        gridPane.add(hashLabel, 1, 1);

        // Define action for hashButton
        hashButton.setOnAction(event -> {
            String message = messageField.getText();

            // Create matrix for hashing
            double[][] matrix = createMatrix(message);

            // Multiply matrix with message hash
            byte[] messageHash = createHash(message.getBytes());
            double[][] hashedMatrix = multiplyMatrix(matrix, messageHash);

            // Hash the resulting matrix
            byte[] hash = createHash(toString(hashedMatrix).getBytes());

            // Convert hash to hexadecimal string
            String hexString = toHexString(hash);

            // Update hashLabel with hash result
            hashLabel.setText(hexString);
        });

        // Set up the scene
        Scene scene = new Scene(gridPane, 400, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cryptographic Hash Function");
        primaryStage.show();
    }

    private double[][] createMatrix(String message) {
        double[][] matrix = new double[MATRIX_SIZE][MATRIX_SIZE];

        // Populate matrix using message characters
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            matrix[i % MATRIX_SIZE][i / MATRIX_SIZE] = c;
        }

        return matrix;
    }

    private byte[] createHash(byte[] input) {
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private double[][] multiplyMatrix(double[][] matrix, byte[] messageHash) {
        double[][] result = new double[MATRIX_SIZE][MATRIX_SIZE];

        // Perform matrix multiplication
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    result[i][j] += matrix[i][k] * messageHash[k + j * MATRIX_SIZE];
                }
            }
        }

        return result;
    }

    private String toString(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                sb.append(matrix[i][j]);
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private String toHexString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < HASH_LENGTH; i++) {
            sb.append(Integer.toHexString(0xFF & hash[i]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
