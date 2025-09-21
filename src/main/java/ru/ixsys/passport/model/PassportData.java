//package ru.ixsyys.passport.model;
//
//public class PassportData {
//
//}

package ru.ixsyys.passport.model;

public class PassportData {
    private String series;
    private String number;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private String birthDate;
    private String birthPlace;
    private String issueDate;
    private String issueAuthority;

    // Конструкторы, геттеры и сеттеры
    public PassportData() {}

    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getBirthPlace() { return birthPlace; }
    public void setBirthPlace(String birthPlace) { this.birthPlace = birthPlace; }

    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }

    public String getIssueAuthority() { return issueAuthority; }
    public void setIssueAuthority(String issueAuthority) { this.issueAuthority = issueAuthority; }

    @Override
    public String toString() {
        return String.format("""
            Серия: %s
            Номер: %s
            Фамилия: %s
            Имя: %s
            Отчество: %s
            Пол: %s
            Дата рождения: %s
            Место рождения: %s
            Дата выдачи: %s
            Кем выдан: %s
            """, series, number, lastName, firstName, middleName,
                gender, birthDate, birthPlace, issueDate, issueAuthority);
    }
}
