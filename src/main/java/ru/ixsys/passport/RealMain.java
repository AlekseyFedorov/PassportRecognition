//package ru.ixsys.passport;
//
//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello, World!");
//    }
//}

package ru.ixsys.passport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nu.pattern.OpenCV;

public class RealMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Инициализация OpenCV
        OpenCV.loadLocally();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        primaryStage.setTitle("Распознавание российского паспорта - ixsyys.ru");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//Особенности алгоритма:
//Предварительная обработка изображения с помощью OpenCV
//Детекция документа и перспективная трансформация
//Улучшение качества изображения для лучшего OCR
//Распознавание текста с помощью Tesseract
//Парсинг результатов с использованием регулярных выражений
//Графический интерфейс на JavaFX
//Алгоритм оптимизирован для работы с российскими паспортами и использует специфические паттерны для извлечения данных.

//Установите Tesseract OCR:
//# Ubuntu/Debian
//sudo apt install tesseract-ocr tesseract-ocr-rus
//# Windows: скачайте с https://github.com/UB-Mannheim/tesseract/wiki

//Скачайте traineddata для русского языка и поместите в src/main/resources/tessdata/

//Запуск приложения:
//mvn clean javafx:run

//passport-recognition/
//        ├── src/main/java/ru/ixsyys/passport/
//        │   ├── Main.java
//│   ├── controller/
//        │   │   └── PassportController.java
//│   ├── service/
//        │   │   ├── PassportService.java
//│   │   └── ImageProcessingService.java
//│   ├── model/
//        │   │   └── PassportData.java
//│   └── util/
//        │       └── OpenCVUtil.java
//├── src/main/resources/
//        │   └── styles/
//        │       └── styles.css
//└── pom.xml