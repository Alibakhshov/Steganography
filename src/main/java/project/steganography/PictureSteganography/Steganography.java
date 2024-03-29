package project.steganography.PictureSteganography;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static project.steganography.PictureSteganography.Utils.*;

public class Steganography extends Application {

  public static File sourceFile;
  public static BufferedImage baseImage, toBeRevealedImage;

  // per colour channel, 1,2,4 or 8 - 2 by default
  public static int bitsPerPixel = 2;

  // Allows file names to be up to 255 characters long
  public static final int NAME_HEADER_SIZE = 255;

  // R G B
  public static final int CHANNELS = 3;

  // Must be a multiple of CHANNELS - 54KB
  public static final int BUFFER_SIZE = CHANNELS * 16 * 1024;

  public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
  public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

  public static String[] IMAGE_EXTENSIONS = Arrays.stream(ImageIO.getReaderFileSuffixes())
      .map(s -> "*." + s)
      .toArray(String[]::new);

  private static Stage displayWindow;

  // TODO: Convert to non-static implementation for a more usable design.

  public static void main(String[] args) {
    launch(args);
  }

  public static long getMaxFileSize() {
    // Calculates maximum file size for the current image

    assert baseImage != null;

    // NB. One pixel taken to encode bitsPerPixel
    long potentialSize =
        (Steganography.baseImage.getWidth() * Steganography.baseImage.getHeight() - 1)
            * Steganography.CHANNELS * Steganography.bitsPerPixel / 8;

    // 1 byte for filename size and a maximum of 255 bytes for filename;
    potentialSize -= 256;

    // 4 bytes for the long that stores the size of the source file;
    potentialSize -= 4;

    return potentialSize;
  }

  public static void setBitsPerPixel(int bitsPerPixel) {
    Steganography.bitsPerPixel = bitsPerPixel;
  }

  public static void compileHide() {
    assert sourceFile.length() <= getMaxFileSize()
        : "The given file is too large to be hidden within the given image";

    BufferedImage resultImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(),
        BufferedImage.TYPE_INT_RGB);

    // Store bitsPerPixel within first pixel for rest of image
    int bitNum;
    for (bitNum = 0; bitNum < 4; bitNum++) {
      if (1 << bitNum == bitsPerPixel) {
        break;
      }
    }
    Color firstCol = new Color(baseImage.getRGB(0, 0));
    int pixR = (firstCol.getRed() & 0xFE) + ((2 & bitNum) >> 1);
    int pixB = (firstCol.getBlue() & 0xFE) + (1 & bitNum);
    resultImage.setRGB(0, 0, new Color(pixR, firstCol.getGreen(), pixB).getRGB());

    // To store indeterminate number of bytes in header
    List<Byte> headerBytes = new ArrayList<>();

    // Filename: length then bytes in UTF-8
    byte[] fileNameBytes = sourceFile.getName().getBytes(StandardCharsets.UTF_8);

    if (fileNameBytes.length > NAME_HEADER_SIZE) {
      Platform.runLater(() -> HiderLayout.statusLabel.setText("Filename too long"));
      return;
    }

    // Cannot overflow byte - enforced with NAME_HEADER_SIZE
    byte nameSize = (byte) fileNameBytes.length;

    headerBytes.add(nameSize);
    addBytesToList(headerBytes, fileNameBytes);

    // Data size
    long dataSize = sourceFile.length();
    addBytesToList(headerBytes, longToByteArray(dataSize));

    // Data and write to image
    try {
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceFile));

      byte[] buf = byteListToByteArray(headerBytes);

      int bufferByte = buf[0];
      int bitPos = 0;
      int bytePos = 0;

      boolean complete = false;

      // Data write loop
      for (int y = 0; y < resultImage.getHeight(); y++) {
        for (int x = (y == 0 ? 1 : 0); x < resultImage.getWidth(); x++) {

          // Base colour
          Color baseCol = new Color(baseImage.getRGB(x, y));
          int[] pixelRGB = new int[]{baseCol.getRed(), baseCol.getGreen(), baseCol.getBlue()};

          int readLen = 0;
          for (int c = 0; c < CHANNELS; c++) {
            if (!complete) {
              if (bitPos >= 8) {
                bitPos = 0;
                bytePos++;
                if (bytePos < buf.length) {
                  bufferByte = buf[bytePos];
                } else {
                  bytePos = 0;
                  buf = new byte[BUFFER_SIZE];
                  readLen = in.read(buf);
                  if (readLen < 0) {
                    // byte stream complete
                    complete = true;
                    break;
                  } else {
                    bufferByte = buf[bytePos];
                  }
                }
              }
              pixelRGB[c] &= (0xFF - numOfBitsToMask(bitsPerPixel));
              pixelRGB[c] +=
                  (bufferByte & (numOfBitsToMask(bitsPerPixel) << (8 - bitsPerPixel - bitPos)))
                      >> (8 - bitsPerPixel - bitPos);

              bitPos += bitsPerPixel;
            }
          }
          resultImage.setRGB(x, y, new Color(pixelRGB[0], pixelRGB[1], pixelRGB[2]).getRGB());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      Platform.runLater(() -> HiderLayout.statusLabel.setText("Error reading file"));
      return;
    }

    saveImage(resultImage, "out");
    Platform.runLater(() -> HiderLayout.statusLabel.setText("Done"));

  }

  public static void compileReveal() {

    // Calculate bits per pixel
    Color firstCol = new Color(toBeRevealedImage.getRGB(0, 0));
    int embeddedBitsPerPixel = 1 << (((firstCol.getRed() & 0x1) << 1) + (firstCol.getBlue() & 0x1));

    Integer fileNameLength = null;
    String fileName = null;
    Long dataSize = null;

    List<Byte> byteList = new ArrayList<Byte>();
    byte currentByte = 0;
    int bitIndex = 0;

    // Start reading in bytes from image
    outer:
    for (int y = 0; y < toBeRevealedImage.getHeight(); y++) {
      for (int x = (y == 0 ? 1 : 0); x < toBeRevealedImage.getWidth(); x++) {
        Color baseCol = new Color(toBeRevealedImage.getRGB(x, y));
        int[] pixelRGB = new int[]{baseCol.getRed(), baseCol.getGreen(), baseCol.getBlue()};

        for (int c = 0; c < CHANNELS; c++) {
          currentByte <<= embeddedBitsPerPixel;
          currentByte += pixelRGB[c] & numOfBitsToMask(embeddedBitsPerPixel);
          bitIndex += embeddedBitsPerPixel;

          // Byte complete
          if (bitIndex >= 8) {
            bitIndex = 0;
            byteList.add(currentByte);
            currentByte = 0;

            if (fileNameLength == null) {
              // No data queue size check needed as fileNameLength is only one byte
              assert byteList.size() == 1 : "Filename length not correctly processed";
              // Fix signed int for byte
              fileNameLength = byteList.get(0) < 0
                  ? byteList.get(0) + 256
                  : Integer.valueOf(byteList.get(0));
              byteList.clear();

            } else if (fileName == null && byteList.size() >= fileNameLength) {
              // Comparison order chosen for efficiency
              assert byteList.size() == fileNameLength
                  : "Filename has not been processed correctly";
              fileName = new String(byteListToByteArray(byteList), StandardCharsets.UTF_8);
              byteList.clear();

            } else if (fileName != null && dataSize == null && byteList.size() >= 8) {
              assert byteList.size() == 8 : "Data size has not been processed correctly";
              // 8 bytes in a long
              dataSize = 0L;
              for (int i = 0; i < 8; i++) {
                // Fix signed int for byte
                dataSize += byteList.get(i) < 0 ? byteList.get(i) + 256 : byteList.get(i);
                dataSize <<= i != 7 ? 8 : 0;
              }
              byteList.clear();

            } else if (dataSize != null && byteList.size() >= dataSize) {
              break outer;
            }
          }
        }

      }
    }

    byte[] dataOut = byteListToByteArray(byteList);

    try {
      FileOutputStream stream = new FileOutputStream(noOverrideFile(fileName));
      stream.write(dataOut);
      stream.close();
    } catch (FileNotFoundException e) {
      Platform
          .runLater(() -> RevealerLayout.statusLabel.setText("Error finding location to write"));
    } catch (IOException e) {
      Platform.runLater(() -> RevealerLayout.statusLabel.setText("Error writing file"));
    }
    Platform.runLater(() -> RevealerLayout.statusLabel.setText("Done"));

  }

  @Override
  public void start(Stage window) {
    displayWindow = window;

    Scene scene = new Scene(MenuLayout.layout(window));
    //scene.getStylesheets().add(getClass().getResource("PicSteganography.css").toExternalForm());
    window.setTitle("Steganography");
    window.setScene(scene);
    window.centerOnScreen();
    window.show();
  }
}
