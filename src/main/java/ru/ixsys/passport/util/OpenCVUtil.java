//package ru.ixsyys.passport.util;
//
//public class OpenCVUtil {
//}

package ru.ixsys.passport.util;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVUtil {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static Mat preprocessImage(Mat source) {
        Mat processed = new Mat();

        // Конвертация в grayscale
        Imgproc.cvtColor(source, processed, Imgproc.COLOR_BGR2GRAY);

        // Гауссово размытие для уменьшения шума
        Imgproc.GaussianBlur(processed, processed, new Size(3, 3), 0);

        // Адаптивная бинаризация
        Imgproc.adaptiveThreshold(processed, processed, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);

        // Морфологические операции
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, kernel);

        return processed;
    }

    public static Mat detectDocument(Mat source) {
        Mat gray = new Mat();
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY);

        // Детекция краев
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);

        // Поиск контуров
        Mat hierarchy = new Mat();
        java.util.List<MatOfPoint> contours = new java.util.ArrayList<>();
        Imgproc.findContours(edges, contours, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Поиск прямоугольного контура (документа)
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);

            if (approxCurve.toArray().length == 4) {
                // Перспективная трансформация для выравнивания документа
                return perspectiveTransform(source, approxCurve);
            }
        }

        return source; // Если документ не найден, возвращаем исходное изображение
    }

    private static Mat perspectiveTransform(Mat source, MatOfPoint2f contour) {
        Point[] points = contour.toArray();

        // Сортировка точек
        java.util.Arrays.sort(points, (p1, p2) -> Double.compare(p1.y, p2.y));

        Point[] topPoints = {points[0], points[1]};
        Point[] bottomPoints = {points[2], points[3]};

        java.util.Arrays.sort(topPoints, (p1, p2) -> Double.compare(p1.x, p2.x));
        java.util.Arrays.sort(bottomPoints, (p1, p2) -> Double.compare(p1.x, p2.x));

        Point tl = topPoints[0];
        Point tr = topPoints[1];
        Point bl = bottomPoints[0];
        Point br = bottomPoints[1];

        double widthA = Math.sqrt(Math.pow(br.x - bl.x, 2) + Math.pow(br.y - bl.y, 2));
        double widthB = Math.sqrt(Math.pow(tr.x - tl.x, 2) + Math.pow(tr.y - tl.y, 2));
        double maxWidth = Math.max(widthA, widthB);

        double heightA = Math.sqrt(Math.pow(tr.x - br.x, 2) + Math.pow(tr.y - br.y, 2));
        double heightB = Math.sqrt(Math.pow(tl.x - bl.x, 2) + Math.pow(tl.y - bl.y, 2));
        double maxHeight = Math.max(heightA, heightB);

        MatOfPoint2f src = new MatOfPoint2f(tl, tr, br, bl);
        MatOfPoint2f dst = new MatOfPoint2f(
                new Point(0, 0),
                new Point(maxWidth - 1, 0),
                new Point(maxWidth - 1, maxHeight - 1),
                new Point(0, maxHeight - 1)
        );

        Mat transform = Imgproc.getPerspectiveTransform(src, dst);
        Mat warped = new Mat();
        Imgproc.warpPerspective(source, warped, transform,
                new Size(maxWidth, maxHeight));

        return warped;
    }
}