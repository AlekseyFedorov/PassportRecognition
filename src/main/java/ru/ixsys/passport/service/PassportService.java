//package ru.ixsyys.passport.service;
//
//public class PassportService {
//}

package ru.ixsys.passport.service;

import ru.ixsys.passport.model.PassportData;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PassportService {

    public ImageProcessingService getImageService() {
        return imageService;
    }

    private final ImageProcessingService imageService;

    public PassportService() {
        this.imageService = new ImageProcessingService();
    }

    public PassportData recognizePassport(String imagePath) {
        String extractedText = imageService.extractTextFromImage(imagePath);
        return parsePassportData(extractedText);
    }

    private PassportData parsePassportData(String text) {
        PassportData data = new PassportData();

        // Паттерны для российского паспорта
        Pattern seriesPattern = Pattern.compile("(?i)серия\\s*([0-9]{2}\\s*[0-9]{2})");
        Pattern numberPattern = Pattern.compile("(?i)номер\\s*([0-9]{6})");
        Pattern namePattern = Pattern.compile("(?i)фамилия\\s*([А-ЯЁ][а-яё]+)");
        Pattern firstNamePattern = Pattern.compile("(?i)имя\\s*([А-ЯЁ][а-яё]+)");
        Pattern middleNamePattern = Pattern.compile("(?i)отчество\\s*([А-ЯЁ][а-яё]+)");
        Pattern genderPattern = Pattern.compile("(?i)пол\\s*([МЖ])");
        Pattern birthDatePattern = Pattern.compile("(?i)дата рождения\\s*([0-9]{2}\\.[0-9]{2}\\.[0-9]{4})");
        Pattern birthPlacePattern = Pattern.compile("(?i)место рождения\\s*([^\\n]+)");
        Pattern issueDatePattern = Pattern.compile("(?i)дата выдачи\\s*([0-9]{2}\\.[0-9]{2}\\.[0-9]{4})");
        Pattern authorityPattern = Pattern.compile("(?i)кем выдан\\s*([^\\n]+)");

        data.setSeries(extractValue(seriesPattern, text));
        data.setNumber(extractValue(numberPattern, text));
        data.setLastName(extractValue(namePattern, text));
        data.setFirstName(extractValue(firstNamePattern, text));
        data.setMiddleName(extractValue(middleNamePattern, text));
        data.setGender(extractValue(genderPattern, text));
        data.setBirthDate(extractValue(birthDatePattern, text));
        data.setBirthPlace(extractValue(birthPlacePattern, text));
        data.setIssueDate(extractValue(issueDatePattern, text));
        data.setIssueAuthority(extractValue(authorityPattern, text));

        return data;
    }

    private String extractValue(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Не распознано";
    }
}