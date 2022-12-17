package project.steganography;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LSB extends Application {

    private static final String MESSAGE_HIDDEN_SUCCESSFULLY = "Message hidden successfully!";
    private static final String MESSAGE_EXTRACTED_SUCCESSFULLY = "Message extracted successfully!";
    private static final String ERROR_HIDING_MESSAGE = "Error hiding message!";
    private static final String ERROR_EXTRACTING_MESSAGE = "Error extracting message!";

    private TextArea messageTextArea;
    private Label statusLabel;
    private Button hideMessageButton;
    private Button extractMessageButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hide Message In Image");

        messageTextArea = new TextArea();
        messageTextArea.setPromptText("Enter message to hide");

        hideMessageButton = new Button("Hide Message");
        hideMessageButton.setOnAction(event -> hideMessage());

        extractMessageButton = new Button("Extract Message");
        extractMessageButton.setOnAction(event -> extractMessage());

        statusLabel = new Label();

        HBox buttonContainer = new HBox(hideMessageButton, extractMessageButton);
        buttonContainer.setSpacing(10);

        VBox root = new VBox(messageTextArea, buttonContainer, statusLabel);
        root.setSpacing(10);

        Scene scene = new Scene(root, 500, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void hideMessage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image to Hide Message In");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(hideMessageButton.getScene().getWindow());
        if (selectedFile == null) {
            return;
        }

        try {
            BufferedImage image = ImageIO.read(selectedFile);
            hideMessageInImage(image, messageTextArea.getText());
            File outputFile = new File(selectedFile.getParentFile(), "hidden_message.png");
            ImageIO.write(image, "png", outputFile);
            statusLabel.setText(MESSAGE_HIDDEN_SUCCESSFULLY);
        } catch (IOException e) {
            statusLabel.setText(ERROR_HIDING_MESSAGE);

        }
    }

    private void extractMessage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image to Extract Message From");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(extractMessageButton.getScene().getWindow());
        if (selectedFile == null) {
            return;
        }

        try {
            BufferedImage image = ImageIO.read(selectedFile);
            String message = extractMessageFromImage(image);
            messageTextArea.setText(message);
            statusLabel.setText(MESSAGE_EXTRACTED_SUCCESSFULLY);
        } catch (IOException e) {
            statusLabel.setText(ERROR_EXTRACTING_MESSAGE);
        }
    }

    private void hideMessageInImage(BufferedImage image, String message) {
        // convert all items to byte arrays: image, message, message length
        byte img[] = getByteData(image);
        byte msg[] = message.getBytes();
        byte len[] = bitConversion(msg.length);
        try {
            encodeText(img, len,  0); // 0 first positiong
            encodeText(img, msg, 32); // 4 bytes of space for length: 4bytes*8bit = 32 bits
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String extractMessageFromImage(BufferedImage image) {
        byte[] decode;
        try {
            decode = decodeText(getByteData(image));
            return (new String(decode, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    private byte[] getByteData(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight(), channels = image.getRaster().getNumBands();
        byte[] result = new byte[width * height * channels];
        image.getRaster().getDataElements(0, 0, width, height, result);
        return result;
    }

    private byte[] bitConversion(int i) {
        byte byte3 = (byte) ((i & 0xFF000000) >>> 24);
        byte byte2 = (byte) ((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte) ((i & 0x0000FF00) >>> 8 );
        byte byte0 = (byte) ((i & 0x000000FF));
        return(new byte[]{byte3, byte2, byte1, byte0});
    }

    private byte[] encodeText(byte[] image, byte[] addition, int offset) {
        // check that the data + offset will fit in the image
        if (addition.length + offset > image.length) {
            throw new IllegalArgumentException("File not long enough!");
        }
        // loop through each addition byte
        for (int i=0; i<addition.length; ++i) {
            // loop through the 8 bits of each byte
            int add = addition[i];
            for (int bit=7; bit>=0; --bit, ++offset) { // ensure the new offset value carries on through both loops
                // assign an integer to b, shifted by bit spaces AND 1
                // a single bit of the current byte
                int b = (add >>> bit) & 1;
                // assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
                // changes the last bit of the byte in the image to be the bit of addition
                image[offset] = (byte)((image[offset] & 0xFE) | b );
            }
        }
        return image;
    }

    private byte[] decodeText(byte[] image) {
        int length = 0;
        int offset  = 32;
        // loop through 32 bytes of data to determine text length
        for (int i=0; i<32; ++i) { // i=24 will also work, as only the 4th byte contains real data
            length = (length << 1) | (image[i] & 1);
        }
        byte[] result = new byte[length];
        // loop through each byte of text
        for (int b=0; b<result.length; ++b ) {
            // loop through each bit within a byte of text
            for (int i=0; i<8; ++i, ++offset) {
                // assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}