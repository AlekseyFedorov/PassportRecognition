package ru.ixsys.passport.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ru.ixsys.passport.model.PassportData;
import ru.ixsys.passport.service.PassportService;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class PassportController {

    @FXML private ImageView originalImageView;
    @FXML private ImageView processedImageView;
    @FXML private TextArea resultTextArea;
    @FXML private Button selectImageButton;
    @FXML private Button recognizeButton;
    @FXML private ProgressIndicator progressIndicator;

    private File selectedFile;
    private final PassportService passportService;

    public PassportController() {
        this.passportService = new PassportService();
    }

    @FXML
    public void initialize() {
        progressIndicator.setVisible(false);
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение паспорта");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Отображаем оригинальное изображение
            Image image = new Image(selectedFile.toURI().toString());
            originalImageView.setImage(image);

            // Отображаем обработанное изображение
            Mat processedMat = passportService.getImageService().processImageForDisplay(selectedFile.getAbsolutePath());
            String tempPath = "temp_display.png";
            Imgcodecs.imwrite(tempPath, processedMat);
            Image processedImage = new Image(new File(tempPath).toURI().toString());
            processedImageView.setImage(processedImage);
            new File(tempPath).delete();

            recognizeButton.setDisable(false);
        }
    }

    @FXML
    private void handleRecognize() {
        if (selectedFile != null) {
            progressIndicator.setVisible(true);
            recognizeButton.setDisable(true);

            new Thread(() -> {
                try {
                    PassportData data = passportService.recognizePassport(selectedFile.getAbsolutePath());

                    javafx.application.Platform.runLater(() -> {
                        resultTextArea.setText(data.toString());
                        progressIndicator.setVisible(false);
                        recognizeButton.setDisable(false);
                    });
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        resultTextArea.setText("Ошибка распознавания: " + e.getMessage());
                        progressIndicator.setVisible(false);
                        recognizeButton.setDisable(false);
                    });
                }
            }).start();
        }
    }

    // Геттер для сервиса (для доступа из контроллера)
    public PassportService getPassportService() {
        return passportService;
    }
}
