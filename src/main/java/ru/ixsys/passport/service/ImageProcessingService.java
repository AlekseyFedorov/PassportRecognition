//package ru.ixsyys.passport.service;
//
//public class ImageProcessingService {
//}

package ru.ixsyys.passport.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import ru.ixsyys.passport.util.OpenCVUtil;

import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ImageProcessingService {

    private final Tesseract tesseract;

    public ImageProcessingService() {
        tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("rus+eng");
        tesseract.setPageSegMode(6); // Единый блок текста
        tesseract.setOcrEngineMode(1);
    }

    public String extractTextFromImage(String imagePath) {
        try {
            Mat image = Imgcodecs.imread(imagePath);
            Mat processed = OpenCVUtil.detectDocument(image);
            processed = OpenCVUtil.preprocessImage(processed);

            // Сохраняем обработанное изображение для OCR
            String tempPath = "temp_processed.png";
            Imgcodecs.imwrite(tempPath, processed);

            String result = tesseract.doOCR(new File(tempPath));
            new File(tempPath).delete();

            return result;
        } catch (TesseractException e) {
            throw new RuntimeException("OCR error: " + e.getMessage(), e);
        }
    }

    public Mat processImageForDisplay(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath);
        Mat processed = OpenCVUtil.detectDocument(image);
        return OpenCVUtil.preprocessImage(processed);
    }
}